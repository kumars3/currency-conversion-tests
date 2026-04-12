package utils;

/**
 * Utility class for validating conversion results.
 *
 * Provides:
 * - Percentage-based tolerance validation
 * - Range calculation helpers
 *
 * Used to compare UI results with expected API values.
 */

public class ToleranceValidator {

    private ToleranceValidator() {
    }

    public static boolean isWithinPercentage(double expected, double actual, double tolerancePercent) {
        double tolerance = expected * tolerancePercent;
        double min = expected - tolerance;
        double max = expected + tolerance;
        return actual >= min && actual <= max;
    }

    public static String buildRangeMessage(double expected, double actual, double tolerancePercent) {
        double tolerance = expected * tolerancePercent;
        double min = expected - tolerance;
        double max = expected + tolerance;

        return String.format(
                "Expected=%.4f, Actual=%.4f, Tolerance=%.2f%%, AllowedRange=[%.4f, %.4f]",
                expected, actual, tolerancePercent * 100, min, max
        );
    }
}