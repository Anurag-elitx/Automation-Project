package com.aliexpress.automation.pages;

import com.aliexpress.automation.utils.JavaScriptUtils;
import com.aliexpress.automation.utils.WaitUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.logging.Logger;

/**
 * Base page class that provides common functionality for all page objects
 */
public class BasePage {
    protected WebDriver driver;
    protected WaitUtils waitUtils;
    protected JavaScriptUtils jsUtils;
    protected Actions actions;
    protected static final Logger LOGGER = Logger.getLogger(BasePage.class.getName());

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        this.jsUtils = new JavaScriptUtils(driver);
        this.actions = new Actions(driver);
    }

    /**
     * Clicks on element with retry mechanism
     *
     * @param element WebElement to click
     */
    protected void click(WebElement element) {
        try {
            waitUtils.waitForElementToBeClickable(element).click();
        } catch (StaleElementReferenceException e) {
            LOGGER.warning("StaleElementReferenceException occurred. Retrying click operation.");
            waitUtils.waitForElementToBeClickable(element).click();
        } catch (ElementClickInterceptedException e) {
            LOGGER.warning("ElementClickInterceptedException occurred. Using JavaScript click.");
            jsUtils.clickElementWithJS(element);
        }
    }

    /**
     * Clicks on element identified by locator
     *
     * @param locator By locator to find element
     */
    protected void click(By locator) {
        click(waitUtils.waitForElementToBeVisible(locator));
    }

    /**
     * Types text into input field
     *
     * @param element WebElement to type into
     * @param text    Text to type
     */
    protected void type(WebElement element, String text) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
        } catch (StaleElementReferenceException e) {
            LOGGER.warning("StaleElementReferenceException occurred. Retrying type operation.");
            waitUtils.waitForElementToBeVisible(element).clear();
            element.sendKeys(text);
        }
    }

    /**
     * Types text into input field identified by locator
     *
     * @param locator By locator to find element
     * @param text    Text to type
     */
    protected void type(By locator, String text) {
        type(waitUtils.waitForElementToBeVisible(locator), text);
    }

    /**
     * Selects option by visible text
     *
     * @param element  Select WebElement
     * @param optionText Text to select
     */
    protected void selectByText(WebElement element, String optionText) {
        try {
            Select select = new Select(waitUtils.waitForElementToBeVisible(element));
            select.selectByVisibleText(optionText);
            LOGGER.info("Selected option: " + optionText);
        } catch (Exception e) {
            LOGGER.severe("Error selecting option: " + e.getMessage());
        }
    }

    /**
     * Selects option by value
     *
     * @param element Select WebElement
     * @param value   Value to select
     */
    protected void selectByValue(WebElement element, String value) {
        try {
            Select select = new Select(waitUtils.waitForElementToBeVisible(element));
            select.selectByValue(value);
            LOGGER.info("Selected value: " + value);
        } catch (Exception e) {
            LOGGER.severe("Error selecting value: " + e.getMessage());
        }
    }

    /**
     * Selects option by index
     *
     * @param element Select WebElement
     * @param index   Index to select
     */
    protected void selectByIndex(WebElement element, int index) {
        try {
            Select select = new Select(waitUtils.waitForElementToBeVisible(element));
            select.selectByIndex(index);
            LOGGER.info("Selected index: " + index);
        } catch (Exception e) {
            LOGGER.severe("Error selecting index: " + e.getMessage());
        }
    }

    /**
     * Hovers over an element
     *
     * @param element WebElement to hover over
     */
    protected void hoverOver(WebElement element) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            actions.moveToElement(element).perform();
            LOGGER.info("Hovered over element");
        } catch (Exception e) {
            LOGGER.severe("Error hovering over element: " + e.getMessage());
        }
    }

    /**
     * Drags and drops an element
     *
     * @param source      Source WebElement
     * @param destination Destination WebElement
     */
    protected void dragAndDrop(WebElement source, WebElement target) {
        try {
            waitUtils.waitForElementToBeVisible(source);
            waitUtils.waitForElementToBeVisible(target);
            actions.dragAndDrop(source, target).perform();
            LOGGER.info("Performed drag and drop");
        } catch (Exception e) {
            LOGGER.severe("Error performing drag and drop: " + e.getMessage());
        }
    }

    /**
     * Performs right click on element
     *
     * @param element WebElement to right-click
     */
    protected void rightClick(WebElement element) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            actions.contextClick(element).perform();
            LOGGER.info("Performed right click");
        } catch (Exception e) {
            LOGGER.severe("Error performing right click: " + e.getMessage());
        }
    }

    /**
     * Performs double click on element
     *
     * @param element WebElement to double-click
     */
    protected void doubleClick(WebElement element) {
        try {
            waitUtils.waitForElementToBeVisible(element);
            actions.doubleClick(element).perform();
            LOGGER.info("Performed double click");
        } catch (Exception e) {
            LOGGER.severe("Error performing double click: " + e.getMessage());
        }
    }

    /**
     * Gets text from element
     *
     * @param element WebElement to get text from
     * @return Text content of the element
     */
    protected String getText(WebElement element) {
        try {
            return waitUtils.waitForElementToBeVisible(element).getText();
        } catch (Exception e) {
            LOGGER.severe("Error getting text from element: " + e.getMessage());
            return "";
        }
    }

    /**
     * Gets text from element identified by locator
     *
     * @param locator By locator to find element
     * @return Text content of the element
     */
    protected String getText(By locator) {
        return getText(waitUtils.waitForElementToBeVisible(locator));
    }

    /**
     * Checks if element is displayed
     *
     * @param element WebElement to check
     * @return true if element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Checks if element identified by locator is displayed
     *
     * @param locator By locator to find element
     * @return true if element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Switches to iframe
     *
     * @param iframe WebElement representing the iframe
     */
    protected void switchToIframe(WebElement iframe) {
        try {
            waitUtils.waitForElementToBeVisible(iframe);
            driver.switchTo().frame(iframe);
            LOGGER.info("Switched to iframe");
        } catch (Exception e) {
            LOGGER.severe("Error switching to iframe: " + e.getMessage());
        }
    }

    /**
     * Switches to iframe by index
     *
     * @param index Index of the iframe
     */
    protected void switchToIframe(int index) {
        try {
            driver.switchTo().frame(index);
            LOGGER.info("Switched to iframe at index: " + index);
        } catch (Exception e) {
            LOGGER.severe("Error switching to iframe: " + e.getMessage());
        }
    }

    /**
     * Switches to iframe by name or id
     *
     * @param nameOrId Name or ID of the iframe
     */
    protected void switchToIframe(String nameOrId) {
        try {
            driver.switchTo().frame(nameOrId);
            LOGGER.info("Switched to iframe with name/id: " + nameOrId);
        } catch (Exception e) {
            LOGGER.severe("Error switching to iframe: " + e.getMessage());
        }
    }

    /**
     * Switches back to default content from iframe
     */
    protected void switchToDefaultContent() {
        try {
            driver.switchTo().defaultContent();
            LOGGER.info("Switched back to default content");
        } catch (Exception e) {
            LOGGER.severe("Error switching to default content: " + e.getMessage());
        }
    }

    /**
     * Accepts alert
     */
    protected void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
            LOGGER.info("Accepted alert");
        } catch (NoAlertPresentException e) {
            LOGGER.warning("No alert present to accept");
        }
    }

    /**
     * Dismisses alert
     */
    protected void dismissAlert() {
        try {
            driver.switchTo().alert().dismiss();
            LOGGER.info("Dismissed alert");
        } catch (NoAlertPresentException e) {
            LOGGER.warning("No alert present to dismiss");
        }
    }

    /**
     * Gets text from alert
     *
     * @return Alert text
     */
    protected String getAlertText() {
        try {
            return driver.switchTo().alert().getText();
        } catch (NoAlertPresentException e) {
            LOGGER.warning("No alert present to get text");
            return "";
        }
    }

    /**
     * Refreshes the current page
     */
    protected void refreshPage() {
        driver.navigate().refresh();
        waitUtils.waitForPageLoad();
        LOGGER.info("Page refreshed");
    }

    /**
     * Navigates back in browser history
     */
    protected void navigateBack() {
        driver.navigate().back();
        waitUtils.waitForPageLoad();
        LOGGER.info("Navigated back");
    }

    /**
     * Navigates forward in browser history
     */
    protected void navigateForward() {
        driver.navigate().forward();
        waitUtils.waitForPageLoad();
        LOGGER.info("Navigated forward");
    }
}