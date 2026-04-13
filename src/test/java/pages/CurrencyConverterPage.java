
package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;

/**
 * Page Object for XE Currency Converter UI.
 *
 * Tasks:
 * - Encapsulate all UI interactions
 * - Provide reusable methods for:
 *      - navigation
 *      - selecting currencies
 *      - entering amount
 *      - retrieving conversion result
 *
 * Note:
 * No business logic or validation is implemented here.
 */

public class CurrencyConverterPage {

    private final Page page;

    private final String fromCurrencyContainer = "#midmarketFromCurrency";
    private final String toCurrencyContainer = "#midmarketToCurrency";
    private final String amountInput = "#amount";
    private final String rateText = "p:has-text('=')";
    //The HTML DOM has not enough attributes to locate paragraph text for converted amount, that's why the alternative
    //XPATH locator is kept for use.
    //private final String rateText = "//*[@id=\"__next\"]/main/div[5]/div/section[1]/div[1]/div/div[1]/div/div[1]/p[1]";   
  
    public CurrencyConverterPage(Page page) {
        this.page = page;
    }

    public void navigateTo(String url) {
    	page.navigate(url);
    }

    public void waitForConverterToBeReady() {
        page.locator(fromCurrencyContainer).getByRole(AriaRole.COMBOBOX).waitFor();
        page.locator(toCurrencyContainer).getByRole(AriaRole.COMBOBOX).waitFor();
        page.locator(amountInput).waitFor();
    }

    public void selectFromCurrency(String currency) {
    	
    	Locator fromCurrencyInputField = 
    	    	page.locator("#midmarketFromCurrency").getByRole(AriaRole.COMBOBOX);
    	    	
    	fromCurrencyInputField.waitFor();
    	    	page.waitForCondition(() -> fromCurrencyInputField.isVisible());
    	    	fromCurrencyInputField.click();
    	    	fromCurrencyInputField.fill(currency);
    	   
    	    	Locator option = page
    	    		    .locator("#midmarketFromCurrency-listbox")
    	    		    .getByRole(AriaRole.OPTION)
    	    		    .filter(new Locator.FilterOptions().setHasText(currency))
    	    		    .first();

    	    		option.click();
    }

    public void selectToCurrency(String currency) {
       
    	Locator toCurrencyInputField = 
    			
    	    	page.locator("#midmarketToCurrency").getByRole(AriaRole.COMBOBOX);    	    	
       	    	page.waitForCondition(() -> toCurrencyInputField.isVisible());
    	    	toCurrencyInputField.click();
    	    	toCurrencyInputField.fill(currency);

    	    	Locator option = page
    	    		    .locator("#midmarketToCurrency-listbox")
    	    		    .getByRole(AriaRole.OPTION)
    	    		    .filter(new Locator.FilterOptions().setHasText(currency))
    	    		    .first();

    	    		option.click();
   	    	 
    }

    public void enterAmount(double amount) {
       	 Locator inputToField = page.locator(amountInput);
    	   inputToField.clear();
    	   inputToField.fill(String.valueOf(amount));

    }

    public void clickConvert() {
               
        Locator convertButton =
        	    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Convert"));

        	convertButton.waitFor();
        	page.waitForCondition(() -> convertButton.isEnabled());
        	convertButton.click();
    }
    public double getConvertedAmount() {
        String raw = page.locator(rateText).first().innerText();
        String rightSide = raw.split("=")[1].trim();
        String numeric = rightSide.replaceAll("[^0-9.]", "");
        return Double.parseDouble(numeric);
    }

    public double convert(String from, String to, double amount) {
        selectFromCurrency(from);
        selectToCurrency(to);
        enterAmount(amount);
        clickConvert();
        return getConvertedAmount();
    }
}