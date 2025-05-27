package com.aliexpress.automation.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

/**
 * Utility class providing various wait mechanisms to handle synchronization issues
 */
public class WaitUtils {
    private static final Logger LOGGER = Logger.getLogger(WaitUtils.class.getName());
    private WebDriver driver;
    private WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public WaitUtils(WebDriver driver, long timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    /**
     * Waits for element to be clickable
     *
     * @param element WebElement to wait for
     * @return WebElement once it's clickable
     */
    public WebElement waitForElementToBeClickable(WebElement element) {
        LOGGER.info("Waiting for element to be clickable");
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits for element to be clickable by locator
     *
     * @param locator By locator to find the element
     * @return WebElement once it's clickable
     */
    public WebElement waitForElementToBeClickable(By locator) {
        LOGGER.info("Waiting for element to be clickable: " + locator.toString());
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for element to be visible
     *
     * @param element WebElement to wait for
     * @return WebElement once it's visible
     */
    public WebElement waitForElementToBeVisible(WebElement element) {
        LOGGER.info("Waiting for element to be visible");
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits for element to be visible by locator
     *
     * @param locator By locator to find the element
     * @return WebElement once it's visible
     */
    public WebElement waitForElementToBeVisible(By locator) {
        LOGGER.info("Waiting for element to be visible: " + locator.toString());
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for page to load completely
     */
    public void waitForPageLoad() {
        LOGGER.info("Waiting for page to load completely");
        wait.until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Waits for Ajax calls to complete
     */
    public void waitForAjax() {
        LOGGER.info("Waiting for AJAX calls to complete");
        wait.until((ExpectedCondition<Boolean>) wd -> {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) wd;
            return (Boolean) jsExecutor.executeScript("return jQuery.active == 0");
        });
    }

    /**
     * Waits for element to be present in the DOM
     *
     * @param locator By locator to find the element
     * @return WebElement once it's present in DOM
     */
    public WebElement waitForPresenceOfElement(By locator) {
        LOGGER.info("Waiting for element presence: " + locator.toString());
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for element to disappear/not be visible
     *
     * @param locator By locator to find the element
     */
    public void waitForElementToDisappear(By locator) {
        LOGGER.info("Waiting for element to disappear: " + locator.toString());
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Custom wait with polling
     *
     * @param timeoutInSeconds Timeout in seconds
     * @param pollingInMillis  Polling interval in milliseconds
     * @return WebDriverWait instance
     */
    public WebDriverWait customWait(long timeoutInSeconds, long pollingInMillis) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingInMillis));
    }
}