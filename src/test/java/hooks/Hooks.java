package hooks;

import com.microsoft.playwright.*;
import context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.ConfigReader;
import utils.ExtentReportManager;
import utils.FileUtils;
import utils.ScenarioArtifactLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Cucumber Hooks for test life cycle management.
 *
 * Responsibilities:
 * - Initialise test context before each scenario
 * - Configure browser (based on config or tags)
 * - Capture screenshots, traces, and videos
 * - Flush reports after execution
 *
 * Note:
 * Browser life cycle is handled per currency pair for isolation.
 */

public class Hooks {

    public static TestContext testContext = new TestContext();

    private static final String SCREENSHOT_DIR = "target/artifacts/screenshots";
    private static final String TRACE_DIR = "target/artifacts/traces";
    private static final String VIDEO_DIR = "target/artifacts/videos";

    @Before
    public void setUp(Scenario scenario) {
        testContext.scenarioName = FileUtils.sanitizeFileName(scenario.getName());
        String browserName = ConfigReader.get("browser");
        for (String tag : scenario.getSourceTagNames()) {
            switch (tag.toLowerCase()) {
                case "@chromium":
                    browserName = "chromium";
                    break;
                case "@firefox":
                    browserName = "firefox";
                    break;
                case "@webkit":
                    browserName = "webkit";
                    break;
            }
        
        }
        testContext.browserName = browserName;
        FileUtils.ensureDirectory(SCREENSHOT_DIR);
        FileUtils.ensureDirectory(TRACE_DIR);
        FileUtils.ensureDirectory(VIDEO_DIR);

        ExtentReportManager.startScenario(scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        ExtentReportManager.flush();
    }

    public static void initBrowser() {
        testContext.playwright = Playwright.create();

        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));
        boolean traceEnabled = Boolean.parseBoolean(ConfigReader.get("trace.enabled"));
        boolean videoEnabled = Boolean.parseBoolean(ConfigReader.get("video.enabled"));

        BrowserType browserType = null;
           switch (testContext.browserName.toLowerCase()) {
            case "firefox":
                browserType = testContext.playwright.firefox();
                break;
            case "webkit":
                browserType = testContext.playwright.webkit();
                break;
            case "chromium":
            	browserType = testContext.playwright.chromium();
            default:
                browserType = testContext.playwright.chromium();
                break;
        }

        testContext.browser = browserType.launch(
                new BrowserType.LaunchOptions().setHeadless(headless)
        );

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();

        if (videoEnabled) {
            contextOptions.setRecordVideoDir(Paths.get(VIDEO_DIR));
            contextOptions.setRecordVideoSize(1280, 720);
        }

        testContext.browserContext = testContext.browser.newContext(contextOptions);

        if (traceEnabled) {
            testContext.browserContext.tracing().start(
                    new Tracing.StartOptions()
                            .setScreenshots(true)
                            .setSnapshots(true)
                            .setSources(true)
            );
        }

        testContext.page = testContext.browserContext.newPage();
        testContext.page.setDefaultTimeout(60000);
        testContext.page.setDefaultNavigationTimeout(60000);
    }

    public static void captureArtifactsForCurrentIteration() {
        boolean screenshotOnFailure = Boolean.parseBoolean(ConfigReader.get("screenshot.on.failure"));
        boolean traceEnabled = Boolean.parseBoolean(ConfigReader.get("trace.enabled"));

        try {
            if (testContext.page != null && screenshotOnFailure) {
                Path screenshotPath = Paths.get(
                        SCREENSHOT_DIR,
                        testContext.scenarioName + "_" + System.nanoTime() + ".png"
                );

                testContext.page.screenshot(new Page.ScreenshotOptions()
                        .setPath(screenshotPath)
                        .setFullPage(true));

                testContext.screenshotPath = screenshotPath;

                if (Files.exists(screenshotPath)) {
                    ScenarioArtifactLogger.logInfo("Screenshot saved at: " + screenshotPath.toAbsolutePath());
                }
            }

            if (traceEnabled && testContext.browserContext != null) {
                Path tracePath = Paths.get(
                        TRACE_DIR,
                        testContext.scenarioName + "_" + System.nanoTime() + ".zip"
                );

                testContext.browserContext.tracing().stop(
                        new Tracing.StopOptions().setPath(tracePath)
                );

                testContext.tracePath = tracePath;

                if (Files.exists(tracePath)) {
                    ScenarioArtifactLogger.logInfo("Trace saved at: " + tracePath.toAbsolutePath());
                }
            }

        } catch (Exception e) {
            System.err.println("Error while capturing screenshot/trace: " + e.getMessage());
        }

        try {
            if (testContext.page != null) {
                Video video = testContext.page.video();
                testContext.page.close();

                if (video != null) {
                    Path videoPath = video.path();

                    Path targetVideoPath = Paths.get(
                            VIDEO_DIR,
                            testContext.scenarioName + "_" + System.nanoTime() + ".webm"
                    );

                    Files.copy(videoPath, targetVideoPath);
                    testContext.videoPath = targetVideoPath;

                    if (Files.exists(targetVideoPath)) {
                        ScenarioArtifactLogger.logInfo("Video saved at: " + targetVideoPath.toAbsolutePath());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error while saving video: " + e.getMessage());
        }
    }

    public static void closeBrowser() {
        try {
            if (testContext.page != null) {
                testContext.page.close();
            }

            if (testContext.browserContext != null) {
                testContext.browserContext.close();
            }

            if (testContext.browser != null) {
                testContext.browser.close();
            }

            if (testContext.playwright != null) {
                testContext.playwright.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing browser: " + e.getMessage());
        } finally {
            testContext.page = null;
            testContext.browserContext = null;
            testContext.browser = null;
            testContext.playwright = null;
        }
    }
}