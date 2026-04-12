**Currency Conversion Test Automation Framework**
- Overview:
  
This project is a Java + Maven + Cucumber (BDD) + Playwright based page object model (POM) automation framework, designed to validate currency conversions using the XE platform.

It supports:

•	UI-based currency conversion validation

•	API-based reference validation (XE API / Mock)

•	JSON-driven test data

•	Rich reporting (Cucumber + Extent Reports)

•	Scalable and maintainable architecture

-Tech Stack:

•	Language >	Java

•	Build Tool >	Maven

•	BDD Framework >	Cucumber

•	UI Automation >	Playwright

•	Reporting >	Extent Reports and Cucumber Reports

•	Test Data >	JSON

•	API Integration >	XE Currency Data API

-Features:

•	Page Object Model (POM) design pattern

•         Behaviour Driven Development (BDD) framework

•	Multi-currency conversion support

•	JSON-driven test execution

•	Mock + Real API (XE) support

•	Tolerance-based validation (±1%)

•	Screenshot, trace, and video capture

•	Scenario-based reporting

•	Cross Browser testing by selecting tags (@chromium, @firefox, etc.)

- Running Tests:
  
•	 Run via Maven

     mvn clean test
     
-	Test Reporting Path:
  
•	Extent Report

            target/extent-report.html
            
•	Cucumber Report

             target/cucumber-reports/
             
-	Test Data
  
•	conversions.json

- References:
  
  •	https://playwright.dev/java/
  
  •	https://cucumber.io/docs/installation/java/
  
  •	https://xecdapi.xe.com/docs/v1/
