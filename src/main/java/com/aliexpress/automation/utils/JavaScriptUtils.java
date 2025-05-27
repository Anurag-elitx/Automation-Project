package com.aliexpress.automation.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.logging.Logger;

/**
 * Utility class for JavaScript operations using JavascriptExecutor
 */
public class JavaScriptUtils {
    private static final Logger LOGGER = Logger.getLogger(JavaScriptUtils.class.getName());
    private WebDriver driver;
    private JavascriptExecutor jsExecutor;

    public JavaScriptUtils(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    /**
     * Clicks on an element using JavaScript
     *
     * @param element WebElement to click
     */
    public void clickElementWithJS(WebElement element) {
        try {
            jsExecutor.executeScript("arguments[0].click();", element);
            LOGGER.info("Clicked element using JavaScript");
        } catch (Exception e) {
            LOGGER.severe("Error clicking element with JavaScript: " + e.getMessage());
        }
    }

    /**
     * Scrolls to element using JavaScript
     *
     * @param element WebElement to scroll to
     */
    public void scrollToElement(WebElement element) {
        try {
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
            LOGGER.info("Scrolled to element using JavaScript");
        } catch (Exception e) {
            LOGGER.severe("Error scrolling to element: " + e.getMessage());
        }
    }

    /**
     * Scrolls page to specified position
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void scrollTo(int x, int y) {
        try {
            jsExecutor.executeScript("window.scrollTo(" + x + ", " + y + ");");
            LOGGER.info("Scrolled to position: x=" + x + ", y=" + y);
        } catch (Exception e) {
            LOGGER.severe("Error scrolling to position: " + e.getMessage());
        }
    }

    /**
     * Scrolls to the bottom of the page
     */
    public void scrollToBottom() {
        try {
            jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            LOGGER.info("Scrolled to bottom of page");
        } catch (Exception e) {
            LOGGER.severe("Error scrolling to bottom: " + e.getMessage());
        }
    }

    /**
     * Scrolls to the top of the page
     */
    public void scrollToTop() {
        try {
            jsExecutor.executeScript("window.scrollTo(0, 0);");
            LOGGER.info("Scrolled to top of page");
        } catch (Exception e) {
            LOGGER.severe("Error scrolling to top: " + e.getMessage());
        }
    }

    /**
     * Highlights an element by changing its background color
     *
     * @param element WebElement to highlight
     */
    public void highlightElement(WebElement element) {
        try {
            String originalStyle = element.getAttribute("style");
            jsExecutor.executeScript(
                    "arguments[0].setAttribute('style', arguments[1] + 'background: yellow; border: 2px solid red;');",
                    element, originalStyle);
            
            // Pause to show the highlighting effect
            Thread.sleep(300);
            
            // Restore original style
            jsExecutor.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);", 
                    element, originalStyle);
                    
            LOGGER.info("Element highlighted using JavaScript");
        } catch (Exception e) {
            LOGGER.severe("Error highlighting element: " + e.getMessage());
        }
    }

    /**
     * Executes JavaScript and returns the result
     *
     * @param script JavaScript to execute
     * @return Result of the script execution
     */
    public Object executeScript(String script) {
        try {
            LOGGER.info("Executing JavaScript: " + script);
            return jsExecutor.executeScript(script);
        } catch (Exception e) {
            LOGGER.severe("Error executing JavaScript: " + e.getMessage());
            return null;
        }
    }

    /**
     * Sets value of an element using JavaScript
     *
     * @param element WebElement to set value
     * @param value   Value to set
     */
    public void setValueToElement(WebElement element, String value) {
        try {
            jsExecutor.executeScript("arguments[0].value='" + value + "';", element);
            LOGGER.info("Set value using JavaScript: " + value);
        } catch (Exception e) {
            LOGGER.severe("Error setting value with JavaScript: " + e.getMessage());
        }
    }

    /**
     * Opens a new tab using JavaScript
     */
    public void openNewTab() {
        try {
            jsExecutor.executeScript("window.open();");
            LOGGER.info("Opened new tab using JavaScript");
        } catch (Exception e) {
            LOGGER.severe("Error opening new tab: " + e.getMessage());
        }
    }

    /**
     * Refreshes the current page using JavaScript
     */
    public void refreshPage() {
        try {
            jsExecutor.executeScript("history.go(0);");
            LOGGER.info("Refreshed page using JavaScript");
        } catch (Exception e) {
            LOGGER.severe("Error refreshing page: " + e.getMessage());
        }
    }

    /**
     * Gets the page title using JavaScript
     *
     * @return Page title
     */
    public String getPageTitle() {
        try {
            return (String) jsExecutor.executeScript("return document.title;");
        } catch (Exception e) {
            LOGGER.severe("Error getting page title: " + e.getMessage());
            return "";
        }
    }
}