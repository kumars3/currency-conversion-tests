package runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/**
 * Cucumber Test Runner using JUnit Platform.
 *
 * Responsibilities:
 * - Configure Cucumber execution engine
 * - Define feature location
 * - Define glue code packages (step definitions + hooks)
 * - Configure reporting plugins (HTML, JSON, JUnit)
 *
 * Note:
 * This is the entry point for executing all Cucumber scenarios via Maven.
 */

@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "stepdefinitions,hooks")
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty,summary,html:target/cucumber-reports/cucumber.html,json:target/cucumber-reports/cucumber.json,junit:target/cucumber-reports/cucumber.xml,com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
)
public class RunCucumberTest {
}