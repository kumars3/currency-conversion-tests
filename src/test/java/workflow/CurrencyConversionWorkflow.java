
package workflow;

import context.TestContext;
import utils.EnvReader;
import hooks.Hooks;
import models.ConversionInput;
import pages.CurrencyConverterPage;
import services.ExchangeRateService;
import utils.ConfigReader;
import utils.ExtentReportManager;
import utils.JsonDataReader;
import utils.ToleranceValidator;

import java.util.List;
import java.util.Map;

/**
 * Core workflow class for currency conversion test execution.
 *
 * Tasks:
 * - Load test data from JSON
 * - Execute UI conversions using Playwright
 * - Call exchange rate service (mock/XE API)
 * - Perform validation with tolerance
 * - Log results into Extent report
 *
 * Note:
 * Each currency pair is executed in isolation with a fresh browser session.
 */

public class CurrencyConversionWorkflow {

    private final TestContext context;
    private final ExchangeRateService exchangeRateService;

    public CurrencyConversionWorkflow(TestContext context, ExchangeRateService exchangeRateService) {
        this.context = context;
        this.exchangeRateService = exchangeRateService;
    }

    public void loadInputs(String filePath) {
        List<ConversionInput> data = JsonDataReader.readConversions(filePath);

        context.conversions.clear();
        context.actualResults.clear();
        context.expectedResults.clear();

        context.conversions.addAll(data);
    }

    public void executeConversions() {
        for (ConversionInput input : context.conversions) {
            String pairName = input.getFrom() + " -> " + input.getTo() + " | Amount: " + input.getAmount();
            ExtentReportManager.startCurrencyPairNode(pairName);

            try {
                Hooks.initBrowser();

                CurrencyConverterPage converterPage = new CurrencyConverterPage(context.page);
                //converterPage.navigateTo(ConfigReader.get("app.url"));
                converterPage.navigateTo(EnvReader.get("XE_APP_URL"));
                converterPage.waitForConverterToBeReady();

                ExtentReportManager.logInfo("Opened converter page");

                double actual = converterPage.convert(
                        input.getFrom(),
                        input.getTo(),
                        input.getAmount()
                );

                context.actualResults.put(input, actual);
                ExtentReportManager.logInfo("Actual UI result: " + actual*input.getAmount());

            } finally {
                Hooks.captureArtifactsForCurrentIteration();
                Hooks.closeBrowser();
                ExtentReportManager.endCurrencyPairNode();
            }
        }
    }

    public void validateResults() {
        boolean validationEnabled =
                Boolean.parseBoolean(ConfigReader.get("xe.validation.enabled"));

        if (!validationEnabled) {
            ExtentReportManager.logInfo("XE validation is disabled. Skipping external API validation.");
            return;
        }

        for (Map.Entry<ConversionInput, Double> entry : context.actualResults.entrySet()) {
            ConversionInput input = entry.getKey();
            double actual = entry.getValue()*input.getAmount();

            double expected = exchangeRateService.getExpectedConvertedAmount(
                    input.getFrom(),
                    input.getTo(),
                    input.getAmount()
            );

            context.expectedResults.put(input, expected);

            boolean valid = ToleranceValidator.isWithinPercentage(expected, actual, 0.01);

            if (!valid) {
                String message =
                        "Validation failed for input " + input + ". " +
                        ToleranceValidator.buildRangeMessage(expected, actual, 0.01);

                ExtentReportManager.logFail(message);
                throw new AssertionError(message);
            }

            ExtentReportManager.logPass(
                    "Validation passed for " + input +
                    " | Expected=" + expected +
                    " | Actual=" + actual
            );
        }
    }
}