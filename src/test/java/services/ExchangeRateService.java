package services;

/**
 * Interface for exchange rate providers.
 *
 * Implementations:
 * - MockXeRateService (for development/testing)
 * - XeRateService (real API integration)
 *
 * This abstraction allows switching between providers
 * without changing test logic.
 */

public interface ExchangeRateService {
    double getExpectedConvertedAmount(String from, String to, double amount);
}