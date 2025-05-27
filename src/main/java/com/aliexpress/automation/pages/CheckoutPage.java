package com.aliexpress.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.logging.Logger;

/**
 * Page Object for AliExpress Checkout Page
 */
public class CheckoutPage extends BasePage {
    private static final Logger LOGGER = Logger.getLogger(CheckoutPage.class.getName());

    // XPath locators
    private final By addressFirstNameXpath = By.xpath("//input[contains(@placeholder, 'First name') or @name='firstName']");
    private final By addressLastNameXpath = By.xpath("//input[contains(@placeholder, 'Last name') or @name='lastName']");
    private final By addressLine1Xpath = By.xpath("//input[contains(@placeholder, 'Address') or @name='addressLine1']");
    private final By addressCityXpath = By.xpath("//input[contains(@placeholder, 'City') or @name='city']");
    private final By addressStateXpath = By.xpath("//select[contains(@id, 'state') or @name='state']");
    private final By addressZipXpath = By.xpath("//input[contains(@placeholder, 'Zip') or @name='zipCode']");
    private final By addressPhoneXpath = By.xpath("//input[contains(@placeholder, 'Phone') or @name='mobileNo']");
    private final By placeOrderButtonXpath = By.xpath("//button[contains(text(), 'Place Order') or contains(@class, 'place-order')]");
    private final By paymentMethodXpath = By.xpath("//div[contains(@class, 'payment-method') or contains(@class, 'payment-option')]");
    private final By creditCardOptionXpath = By.xpath("//div[contains(@class, 'credit-card') or .//span[contains(text(), 'Credit Card')]]");
    private final By payPalOptionXpath = By.xpath("//div[contains(@class, 'paypal') or .//span[contains(text(), 'PayPal')]]");
    private final By orderSummaryXpath = By.xpath("//div[contains(@class, 'order-summary') or contains(@class, 'summary-section')]");

    // CSS selectors
    private final By addressFirstNameCss = By.cssSelector("input[placeholder*='First name'], input[name='firstName']");
    private final By addressLastNameCss = By.cssSelector("input[placeholder*='Last name'], input[name='lastName']");
    private final By addressLine1Css = By.cssSelector("input[placeholder*='Address'], input[name='addressLine1']");
    private final By addressCityCss = By.cssSelector("input[placeholder*='City'], input[name='city']");
    private final By addressStateCss = By.cssSelector("select#state, select[name='state']");
    private final By addressZipCss = By.cssSelector("input[placeholder*='Zip'], input[name='zipCode']");
    private final By addressPhoneCss = By.cssSelector("input[placeholder*='Phone'], input[name='mobileNo']");
    private final By placeOrderButtonCss = By.cssSelector("button.place-order, button.submit-order");
    private final By paymentMethodCss = By.cssSelector(".payment-method, .payment-option");
    private final By creditCardOptionCss = By.cssSelector(".credit-card-option, .payment-option.card");
    private final By payPalOptionCss = By.cssSelector(".paypal-option, .payment-option.paypal");
    private final By orderSummaryCss = By.cssSelector(".order-summary, .summary-section");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Fills shipping address information
     *
     * @param firstName First name
     * @param lastName  Last name
     * @param address   Address line 1
     * @param city      City
     * @param state     State
     * @param zipCode   Zip code
     * @param phone     Phone number
     */
    public void fillShippingAddress(String firstName, String lastName, String address, String city, String state, String zipCode, String phone) {
        try {
            LOGGER.info("Filling shipping address information");
            
            // Fill first name
            WebElement firstNameInput = isElementDisplayed(addressFirstNameXpath) ?
                driver.findElement(addressFirstNameXpath) :
                driver.findElement(addressFirstNameCss);
            type(firstNameInput, firstName);
            
            // Fill last name
            WebElement lastNameInput = isElementDisplayed(addressLastNameXpath) ?
                driver.findElement(addressLastNameXpath) :
                driver.findElement(addressLastNameCss);
            type(lastNameInput, lastName);
            
            // Fill address line 1
            WebElement addressInput = isElementDisplayed(addressLine1Xpath) ?
                driver.findElement(addressLine1Xpath) :
                driver.findElement(addressLine1Css);
            type(addressInput, address);
            
            // Fill city
            WebElement cityInput = isElementDisplayed(addressCityXpath) ?
                driver.findElement(addressCityXpath) :
                driver.findElement(addressCityCss);
            type(cityInput, city);
            
            // Select state
            WebElement stateSelect = isElementDisplayed(addressStateXpath) ?
                driver.findElement(addressStateXpath) :
                driver.findElement(addressStateCss);
            selectByText(stateSelect, state);
            
            // Fill zip code
            WebElement zipInput = isElementDisplayed(addressZipXpath) ?
                driver.findElement(addressZipXpath) :
                driver.findElement(addressZipCss);
            type(zipInput, zipCode);
            
            // Fill phone number
            WebElement phoneInput = isElementDisplayed(addressPhoneXpath) ?
                driver.findElement(addressPhoneXpath) :
                driver.findElement(addressPhoneCss);
            type(phoneInput, phone);
            
            LOGGER.info("Successfully filled shipping address");
        } catch (Exception e) {
            LOGGER.severe("Error filling shipping address: " + e.getMessage());
        }
    }

    /**
     * Selects payment method
     *
     * @param paymentMethod Payment method to select (e.g., "Credit Card", "PayPal")
     */
    public void selectPaymentMethod(String paymentMethod) {
        try {
            LOGGER.info("Selecting payment method: " + paymentMethod);
            
            switch (paymentMethod.toLowerCase()) {
                case "credit card":
                    WebElement creditCardOption = isElementDisplayed(creditCardOptionXpath) ?
                        driver.findElement(creditCardOptionXpath) :
                        driver.findElement(creditCardOptionCss);
                    jsUtils.scrollToElement(creditCardOption);
                    click(creditCardOption);
                    break;
                    
                case "paypal":
                    WebElement payPalOption = isElementDisplayed(payPalOptionXpath) ?
                        driver.findElement(payPalOptionXpath) :
                        driver.findElement(payPalOptionCss);
                    jsUtils.scrollToElement(payPalOption);
                    click(payPalOption);
                    break;
                    
                default:
                    LOGGER.warning("Unsupported payment method: " + paymentMethod);
            }
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully selected payment method: " + paymentMethod);
        } catch (Exception e) {
            LOGGER.severe("Error selecting payment method: " + e.getMessage());
        }
    }

    /**
     * Gets order summary text
     *
     * @return Order summary text
     */
    public String getOrderSummary() {
        try {
            LOGGER.info("Getting order summary");
            WebElement summaryElement = isElementDisplayed(orderSummaryXpath) ?
                driver.findElement(orderSummaryXpath) :
                driver.findElement(orderSummaryCss);
                
            jsUtils.scrollToElement(summaryElement);
            return getText(summaryElement);
        } catch (Exception e) {
            LOGGER.severe("Error getting order summary: " + e.getMessage());
            return "";
        }
    }

    /**
     * Clicks place order button
     *
     * @return OrderConfirmationPage instance
     */
    public OrderConfirmationPage placeOrder() {
        try {
            LOGGER.info("Placing order");
            WebElement placeOrderButton = isElementDisplayed(placeOrderButtonXpath) ?
                driver.findElement(placeOrderButtonXpath) :
                driver.findElement(placeOrderButtonCss);
                
            jsUtils.scrollToElement(placeOrderButton);
            click(placeOrderButton);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully placed order");
            return new OrderConfirmationPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error placing order: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Fills credit card information
     *
     * @param cardNumber  Card number
     * @param nameOnCard  Name on card
     * @param expiryMonth Expiry month
     * @param expiryYear  Expiry year
     * @param cvv         CVV code
     */
    public void fillCreditCardInfo(String cardNumber, String nameOnCard, String expiryMonth, String expiryYear, String cvv) {
        try {
            LOGGER.info("Filling credit card information");
            
            // Select Credit Card payment method first
            selectPaymentMethod("Credit Card");
            
            // Find credit card fields
            WebElement cardNumberInput = driver.findElement(By.cssSelector("input[name*='cardNumber'], #cardNumber"));
            WebElement nameInput = driver.findElement(By.cssSelector("input[name*='cardHolder'], #cardName"));
            WebElement expiryMonthSelect = driver.findElement(By.cssSelector("select[name*='month'], #expiryMonth"));
            WebElement expiryYearSelect = driver.findElement(By.cssSelector("select[name*='year'], #expiryYear"));
            WebElement cvvInput = driver.findElement(By.cssSelector("input[name*='cvv'], #cvv"));
            
            // Fill card details
            type(cardNumberInput, cardNumber);
            type(nameInput, nameOnCard);
            selectByValue(expiryMonthSelect, expiryMonth);
            selectByValue(expiryYearSelect, expiryYear);
            type(cvvInput, cvv);
            
            LOGGER.info("Successfully filled credit card information");
        } catch (Exception e) {
            LOGGER.severe("Error filling credit card information: " + e.getMessage());
        }
    }
}