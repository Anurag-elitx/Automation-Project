package com.aliexpress.automation.tests.listener;

import com.aliexpress.automation.base.BaseTest;
import com.aliexpress.automation.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Test class demonstrating TestNG listener functionality
 * Using ITestListener implementation from com.aliexpress.automation.listeners.TestListener
 */
@Listeners(com.aliexpress.automation.listeners.TestListener.class)
public class ListenerTest extends BaseTest {

    /**
     * Test that will pass - to demonstrate onTestSuccess
     */
    @Test
    public void testPassingTest() {
        LOGGER.info("Starting test: testPassingTest");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Verify home page loaded
        Assert.assertTrue(driver.getTitle().contains("AliExpress") || 
                         driver.getCurrentUrl().contains("aliexpress"), 
            "Should be on AliExpress page");
            
        LOGGER.info("Passing test completed successfully");
    }
    
    /**
     * Test that will fail - to demonstrate onTestFailure
     */
    @Test
    public void testFailingTest() {
        LOGGER.info("Starting test: testFailingTest");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // This assertion will fail
        Assert.assertEquals(driver.getTitle(), "This Title Does Not Exist", 
            "This test is intentionally failing to demonstrate TestListener.onTestFailure()");
            
        LOGGER.info("This line should not be reached");
    }
    
    /**
     * Test that will be skipped - to demonstrate onTestSkipped
     */
    @Test(dependsOnMethods = {"testFailingTest"})
    public void testSkippedTest() {
        LOGGER.info("Starting test: testSkippedTest");
        
        // This test will be skipped because it depends on a failing test
        Assert.assertTrue(true, "This test should be skipped");
    }
    
    /**
     * Test with throwable - to demonstrate exception handling in listener
     */
    @Test
    public void testWithException() {
        LOGGER.info("Starting test: testWithException");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Throw an exception to demonstrate exception handling
        throw new RuntimeException("This is a test exception to demonstrate listener exception handling");
    }
    
    /**
     * Test with intentional timeout - to demonstrate timeout handling
     */
    @Test(timeOut = 1000) // 1 second timeout
    public void testWithTimeout() {
        LOGGER.info("Starting test: testWithTimeout");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        try {
            // Sleep for 2 seconds to exceed timeout
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        LOGGER.info("This line should not be reached due to timeout");
    }
}