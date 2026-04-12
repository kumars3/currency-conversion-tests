package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * Manages Extent Report life cycle and logging.
 *
 * Tasks:
 * - Initialise report
 * - Create scenario and currency-pair nodes
 * - Log info, pass, and failure messages
 * - Flush report at the end of execution
 *
 * Note:
 * Uses ThreadLocal to support parallel execution.
 */

public class ExtentReportManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> scenarioNode = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> pairNode = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    public static ExtentReports getExtentReports() {
        if (extentReports == null) {
            ExtentSparkReporter sparkReporter =
                    new ExtentSparkReporter("target/extent-reports/ExtentReport.html");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
        }
        return extentReports;
    }

    public static void startScenario(String scenarioName) {
        ExtentTest test = getExtentReports().createTest(scenarioName);
        scenarioNode.set(test);
    }

    public static void startCurrencyPairNode(String nodeName) {
        ExtentTest parent = scenarioNode.get();
        if (parent == null) {
            throw new IllegalStateException("Scenario node not initialized.");
        }
        pairNode.set(parent.createNode(nodeName));
    }

    public static void logInfo(String message) {
        if (pairNode.get() != null) {
            pairNode.get().info(message);
        } else if (scenarioNode.get() != null) {
            scenarioNode.get().info(message);
        }
    }

    public static void logPass(String message) {
        if (pairNode.get() != null) {
            pairNode.get().pass(message);
        } else if (scenarioNode.get() != null) {
            scenarioNode.get().pass(message);
        }
    }

    public static void logFail(String message) {
        if (pairNode.get() != null) {
            pairNode.get().fail(message);
        } else if (scenarioNode.get() != null) {
            scenarioNode.get().fail(message);
        }
    }

    public static void endCurrencyPairNode() {
        pairNode.remove();
    }

    public static void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}