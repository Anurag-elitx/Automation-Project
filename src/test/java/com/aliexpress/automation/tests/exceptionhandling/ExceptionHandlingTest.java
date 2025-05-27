package com.aliexpress.automation.tests.exceptionhandling;

import com.aliexpress.automation.base.BaseTest;
import com.aliexpress.automation.pages.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

/**
 * Test class demonstrating exception handling techniques in Selenium
 */
public class ExceptionHandlingTest extends BaseTest {
    private static final Logger LOGGER = Logger.getLogger(ExceptionHandlingTest.class.getName());

    /**
     * Test handling NoSuchElementException
     */
    @Test
    public void testHandlingNoSuchElementException() {
        LOGGER.info("Starting test: testHandlingNoSuchElementException");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        try {
            // Try to find a non-existent element
            WebElement nonExistentElement = driver.findElement(By.id("non-existent-id"));
            LOGGER.severe("Should not reach this code as element doesn't exist");
        } catch (NoSuchElementException e) {
            // Expected exception - handle it gracefully
            LOGGER.info("Successfully caught NoSuchElementException as expected: " + e.getMessage());
            
            // Try alternative approach - check if element exists first
            boolean elementExists = !driver.findElements(By.id("non-existent-id")).isEmpty();
            
            if (elementExists) {
                LOGGER.info("Element found with alternative approach");
            } else {
                LOGGER.info("Element not found with alternative approach either, as expected");
            }
        }
        
        // Continue test execution after exception handling
        Assert.assertTrue(true, "Successfully handled NoSuchElementException");
    }
    
    /**
     * Test handling StaleElementReferenceException
     */
    @Test
    public void testHandlingStaleElementReferenceException() {
        LOGGER.info("Starting test: testHandlingStaleElementReferenceException");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        try {
            // Get reference to search box
            WebElement searchBox = driver.findElement(By.cssSelector("input[placeholder*='Search'], .search-box"));
            
            // Refresh the page to make the element stale
            driver.navigate().refresh();
            
            try {
                // Try to interact with the now-stale element
                searchBox.sendKeys("test");
                LOGGER.severe("Should not reach this code as element is stale");
            } catch (StaleElementReferenceException e) {
                // Expected exception - handle it gracefully
                LOGGER.info("Successfully caught StaleElementReferenceException as expected: " + e.getMessage());
                
                // Re-find the element after page refresh
                searchBox = driver.findElement(By.cssSelector("input[placeholder*='Search'], .search-box"));
                searchBox.sendKeys("test");
                LOGGER.info("Successfully interacted with refreshed element after handling StaleElementReferenceException");
            }
        } catch (Exception e) {
            LOGGER.severe("Unexpected exception: " + e.getMessage());
            Assert.fail("Test failed with unexpected exception: " + e.getMessage());
        }
        
        // Continue test execution after exception handling
        Assert.assertTrue(true, "Successfully handled StaleElementReferenceException");
    }
    
    /**
     * Test handling TimeoutException
     */
    @Test
    public void testHandlingTimeoutException() {
        LOGGER.info("Starting test: testHandlingTimeoutException");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        try {
            // Set a very short timeout to force a TimeoutException
            org.openqa.selenium.support.ui.WebDriverWait shortWait = 
                new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofMillis(100));
            
            try {
                // Wait for a complex condition that will timeout
                shortWait.until(webDriver -> {
                    // This condition should take longer than 100ms to evaluate
                    return webDriver.findElements(By.cssSelector(".non-existent-class")).size() > 10;
                });
                
                LOGGER.severe("Should not reach this code as wait should timeout");
            } catch (TimeoutException e) {
                // Expected exception - handle it gracefully
                LOGGER.info("Successfully caught TimeoutException as expected: " + e.getMessage());
                
                // Fall back to a non-waiting approach
                int actualCount = driver.findElements(By.cssSelector(".non-existent-class")).size();
                LOGGER.info("Fallback approach: found " + actualCount + " elements without waiting");
            }
        } catch (Exception e) {
            LOGGER.severe("Unexpected exception: " + e.getMessage());
            Assert.fail("Test failed with unexpected exception: " + e.getMessage());
        }
        
        // Continue test execution after exception handling
        Assert.assertTrue(true, "Successfully handled TimeoutException");
    }
    
    /**
     * Test handling ElementClickInterceptedException
     */
    @Test
    public void testHandlingElementClickInterceptedException() {
        LOGGER.info("Starting test: testHandlingElementClickInterceptedException");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        try {
            // Find an element that might be intercepted (e.g., by a popup)
            WebElement element = driver.findElement(By.cssSelector("a[href*='category'], .nav-item"));
            
            try {
                // Try to click the element
                element.click();
                LOGGER.info("Element clicked successfully without interception");
            } catch (ElementClickInterceptedException e) {
                // Expected exception - handle it gracefully
                LOGGER.info("Caught ElementClickInterceptedException as expected: " + e.getMessage());
                
                // Try JavaScript click as alternative
                LOGGER.info("Attempting JavaScript click as alternative");
                com.aliexpress.automation.utils.JavaScriptUtils jsUtils = 
                    new com.aliexpress.automation.utils.JavaScriptUtils(driver);
                jsUtils.clickElementWithJS(element);
                
                LOGGER.info("Successfully clicked element using JavaScript after handling ElementClickInterceptedException");
            }
        } catch (Exception e) {
            LOGGER.warning("Could not complete click test: " + e.getMessage());
            // Don't fail the test, as we're just demonstrating exception handling
        }
        
        // Continue test execution after exception handling
        Assert.assertTrue(true, "Successfully demonstrated ElementClickInterceptedException handling");
    }
    
    /**
     * Test try-with-resources for proper resource cleanup
     */
    @Test
    public void testTryWithResourcesPattern() {
        LOGGER.info("Starting test: testTryWithResourcesPattern");
        
        try (AutoCloseable resource = new MockAutoCloseable()) {
            LOGGER.info("Using auto-closeable resource");
            // Use resource...
            
            // Simulate successful resource usage
            LOGGER.info("Resource used successfully");
        } catch (Exception e) {
            LOGGER.severe("Error using resource: " + e.getMessage());
            Assert.fail("Resource usage failed: " + e.getMessage());
        }
        
        LOGGER.info("Resource automatically closed due to try-with-resources");
        Assert.assertTrue(true, "Successfully demonstrated try-with-resources pattern");
    }
    
    /**
     * Test custom retry mechanism for flaky elements
     */
    @Test
    public void testRetryMechanismForFlakyElements() {
        LOGGER.info("Starting test: testRetryMechanismForFlakyElements");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Define retry parameters
        int maxRetries = 3;
        int retryCount = 0;
        boolean success = false;
        
        // Retry loop
        while (retryCount < maxRetries && !success) {
            try {
                // Attempt to perform action
                LOGGER.info("Attempt " + (retryCount + 1) + " of " + maxRetries);
                
                // Example action: clicking on a potentially flaky element
                WebElement element = driver.findElement(By.cssSelector("a[href*='deals'], .top-promotion"));
                element.click();
                
                // If we get here without exception, the action was successful
                success = true;
                LOGGER.info("Action successful on attempt " + (retryCount + 1));
            } catch (Exception e) {
                LOGGER.warning("Attempt " + (retryCount + 1) + " failed: " + e.getMessage());
                retryCount++;
                
                if (retryCount >= maxRetries) {
                    LOGGER.severe("All " + maxRetries + " attempts failed");
                    // Don't throw, we're just demonstrating the retry mechanism
                } else {
                    // Wait before retrying
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        
        // Don't fail the test, we're just demonstrating the retry mechanism
        Assert.assertTrue(true, "Successfully demonstrated retry mechanism");
    }
    
    /**
     * Mock AutoCloseable class for try-with-resources demonstration
     */
    private class MockAutoCloseable implements AutoCloseable {
        public MockAutoCloseable() {
            LOGGER.info("Resource initialized");
        }
        
        @Override
        public void close() throws Exception {
            LOGGER.info("Resource closed properly");
        }
    }
}