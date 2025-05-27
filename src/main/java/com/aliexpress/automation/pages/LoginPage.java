package com.aliexpress.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.logging.Logger;

/**
 * Page Object for AliExpress Login Page
 */
public class LoginPage extends BasePage {
    private static final Logger LOGGER = Logger.getLogger(LoginPage.class.getName());

    // XPath locators
    private final By emailInputXpath = By.xpath("//input[@type='email' or @id='email' or @name='email']");
    private final By passwordInputXpath = By.xpath("//input[@type='password' or @id='password' or @name='password']");
    private final By loginButtonXpath = By.xpath("//button[contains(@class, 'login') or contains(text(), 'Sign In')]");
    private final By registerLinkXpath = By.xpath("//a[contains(@class, 'register') or contains(text(), 'Register') or contains(text(), 'Create account')]");
    private final By forgotPasswordXpath = By.xpath("//a[contains(text(), 'Forgot') or contains(text(), 'Reset password')]");
    private final By errorMessageXpath = By.xpath("//div[contains(@class, 'error-message') or contains(@class, 'error-notice')]");
    private final By otherLoginOptionsXpath = By.xpath("//div[contains(@class, 'other-login') or contains(text(), 'Or sign in with')]");

    // CSS selectors
    private final By emailInputCss = By.cssSelector("input[type='email'], #email, input[name='email']");
    private final By passwordInputCss = By.cssSelector("input[type='password'], #password, input[name='password']");
    private final By loginButtonCss = By.cssSelector("button.login-submit, button.sign-in-button");
    private final By registerLinkCss = By.cssSelector("a.register-link, a.create-account-link");
    private final By forgotPasswordCss = By.cssSelector("a.forgot-password, a.reset-password-link");
    private final By errorMessageCss = By.cssSelector(".error-message, .error-notice, .login-error");
    private final By otherLoginOptionsCss = By.cssSelector(".other-login-methods, .login-with-social");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Performs login with credentials
     *
     * @param email    Email address
     * @param password Password
     * @return HomePage instance if login successful
     */
    public HomePage login(String email, String password) {
        try {
            LOGGER.info("Attempting login with email: " + email);
            
            // Enter email
            WebElement emailElement = isElementDisplayed(emailInputXpath) ?
                driver.findElement(emailInputXpath) :
                driver.findElement(emailInputCss);
            type(emailElement, email);
            
            // Enter password
            WebElement passwordElement = isElementDisplayed(passwordInputXpath) ?
                driver.findElement(passwordInputXpath) :
                driver.findElement(passwordInputCss);
            type(passwordElement, password);
            
            // Click login button
            WebElement loginButton = isElementDisplayed(loginButtonXpath) ?
                driver.findElement(loginButtonXpath) :
                driver.findElement(loginButtonCss);
            click(loginButton);
            
            waitUtils.waitForPageLoad();
            
            // Check for error message
            if (isElementDisplayed(errorMessageXpath) || isElementDisplayed(errorMessageCss)) {
                LOGGER.warning("Login failed - error message displayed");
                throw new RuntimeException("Login failed");
            }
            
            LOGGER.info("Login successful");
            return new HomePage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error during login: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Navigates to registration page
     *
     * @return RegisterPage instance
     */
    public RegisterPage navigateToRegister() {
        try {
            LOGGER.info("Navigating to registration page");
            WebElement registerLink = isElementDisplayed(registerLinkXpath) ?
                driver.findElement(registerLinkXpath) :
                driver.findElement(registerLinkCss);
            click(registerLink);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully navigated to register page");
            return new RegisterPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error navigating to register page: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Clicks on forgot password link
     *
     * @return ForgotPasswordPage instance
     */
    public ForgotPasswordPage clickForgotPassword() {
        try {
            LOGGER.info("Clicking forgot password link");
            WebElement forgotPasswordLink = isElementDisplayed(forgotPasswordXpath) ?
                driver.findElement(forgotPasswordXpath) :
                driver.findElement(forgotPasswordCss);
            click(forgotPasswordLink);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully navigated to forgot password page");
            return new ForgotPasswordPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error clicking forgot password: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Gets error message if present
     *
     * @return Error message text or empty string if no error
     */
    public String getErrorMessage() {
        try {
            if (isElementDisplayed(errorMessageXpath)) {
                return getText(errorMessageXpath);
            } else if (isElementDisplayed(errorMessageCss)) {
                return getText(errorMessageCss);
            } else {
                return "";
            }
        } catch (Exception e) {
            LOGGER.severe("Error getting error message: " + e.getMessage());
            return "";
        }
    }

    /**
     * Checks if login page is loaded properly
     *
     * @return true if login page is loaded, false otherwise
     */
    public boolean isLoginPageLoaded() {
        try {
            return isElementDisplayed(emailInputCss) && 
                   isElementDisplayed(passwordInputCss) &&
                   isElementDisplayed(loginButtonCss);
        } catch (Exception e) {
            LOGGER.severe("Error checking if login page is loaded: " + e.getMessage());
            return false;
        }
    }
}