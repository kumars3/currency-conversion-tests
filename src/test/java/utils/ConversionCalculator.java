package utils;

/**
 * Utility class for performing currency conversion calculations.
 *
 * Tasks:
 * - Calculate expected converted amount based on exchange rate
 * - Support validation logic
 *
 * Note:
 * Used to compute expected values for comparison with UI/API results.
 */

public class ConversionCalculator {

    private ConversionCalculator() {
    }

    public static double round(double value, int scale) {
        double factor = Math.pow(10, scale);
        return Math.round(value * factor) / factor;
    }
}