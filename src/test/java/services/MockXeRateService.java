package services;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock implementation of ExchangeRateService.
 *
 * Used for:
 * - Framework development
 * - Stable test execution without external dependency
 *
 * Returns predefined static rates for currency pairs.
 */

public class MockXeRateService implements ExchangeRateService {

    private static final Map<String, Double> MOCK_RATES = new HashMap<>();

    static {
        MOCK_RATES.put("USD_EUR", 0.85298);
        MOCK_RATES.put("GBP_INR", 125.16735);
        MOCK_RATES.put("EUR_JPY", 186.72650);
    }

    @Override
    public double getExpectedConvertedAmount(String from, String to, double amount) {
        String key = from + "_" + to;
        Double rate = MOCK_RATES.get(key);

        if (rate == null) {
            throw new RuntimeException("No mock exchange rate configured for: " + key);
        }

        return amount * rate;
    }
}