package context;

import com.microsoft.playwright.*;
import models.ConversionInput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared test context for storing runtime data.
 *
 * Holds:
 * - Playwright objects (browser, page, context)
 * - Test data (currency pairs)
 * - Results (actual and expected values)
 * - Scenario metadata
 *
 * Acts as a central data container across layers.
 */

public class TestContext {
    public Playwright playwright;
    public Browser browser;
    public BrowserContext browserContext;
    public Page page;

    public List<ConversionInput> conversions = new ArrayList<>();
    public Map<ConversionInput, Double> actualResults = new LinkedHashMap<>();
    public Map<ConversionInput, Double> expectedResults = new LinkedHashMap<>();

    public String scenarioName;
    public Path videoPath;
    public Path tracePath;
    public Path screenshotPath;
    public String browserName;
}