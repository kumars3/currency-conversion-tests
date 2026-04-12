package utils;

/**
 * Utility class for logging execution artifacts to Extent Report.
 *
 * Tasks:
 * - Log informational messages
 * - Attach execution details (screenshots, traces, videos)
 * - Enhance report readability
 *
 * Note:
 * Acts as a wrapper over ExtentReportManager for cleaner logging usage.
 */

public class ScenarioArtifactLogger {

    private ScenarioArtifactLogger() {
    }

    public static void logInfo(String message) {
        ExtentReportManager.logInfo(message);
    }
}