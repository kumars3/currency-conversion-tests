package services;

import utils.ConfigReader;

/**
 * Factory class for selecting exchange rate provider.
 *
 * Provider is controlled via configuration:
 * - mock
 * - xe
 *
 * Enables flexible switching between mock and real API.
 */

public class ExchangeRateServiceFactory {

    private ExchangeRateServiceFactory() {
    }

    public static ExchangeRateService create() {
        String provider = ConfigReader.get("exchange.rate.provider");

        if (provider == null || provider.isBlank()) {
            throw new RuntimeException("Missing config: exchange.rate.provider");
        }

        switch (provider.toLowerCase()) {
            case "mock":
                return new MockXeRateService();
            case "xe":
                return new XeRateService();
            default:
                throw new RuntimeException("Unsupported exchange rate provider: " + provider);
        }
    }
}