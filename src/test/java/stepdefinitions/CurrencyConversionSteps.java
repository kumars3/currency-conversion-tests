
package stepdefinitions;

import context.TestContext;
import hooks.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import services.ExchangeRateService;
import services.ExchangeRateServiceFactory;
import workflow.CurrencyConversionWorkflow;

/**
 * Step Definitions for Currency Conversion Feature. *
 * This class acts as the BDD layer and delegates execution
 * to the workflow class without containing business logic. *
 *  
 * Tasks:
 * - Map Gherkin steps to Java methods
 * - Trigger workflow execution
 * - Keep steps clean and readable
 */

public class CurrencyConversionSteps {

	private final TestContext context = Hooks.testContext;
	private CurrencyConversionWorkflow workflow;

	@Given("I load all currency conversion test data from {string}")
	public void iLoadAllCurrencyConversionTestDataFrom(String filePath) {
		ExchangeRateService exchangeRateService = ExchangeRateServiceFactory.create();
		workflow = new CurrencyConversionWorkflow(context, exchangeRateService);
		workflow.loadInputs(filePath);
	}

	@When("I perform all currency conversions in the application")
	public void iPerformAllCurrencyConversionsInTheApplication() {
		workflow.executeConversions();
	}

	@Then("each converted result should match XE within 1 percent tolerance")
	public void eachConvertedResultShouldMatchXEWithin1PercentTolerance() {
		workflow.validateResults();
	}
}
