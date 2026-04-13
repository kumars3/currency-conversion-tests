Feature: Currency conversion validation for JSON-driven currency pairs

  @chromium @regression    
  Scenario: Validate all currency pairs from conversions.json against reference exchange rates
   Given I load all currency conversion test data from "testdata/conversions.json"
   When I perform all currency conversions in the application
   Then each converted result should match XE within 1 percent tolerance  