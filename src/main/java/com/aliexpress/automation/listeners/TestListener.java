package com.aliexpress.automation.listeners;

import com.aliexpress.automation.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.logging.Logger;

/**
 * TestNG listener implementation for test execution reporting and screenshots
 * Implements ITestListener to hook into TestNG lifecycle events
 */
public class TestListener implements ITestListener {
    private static final Logger LOGGER = Logger.getLogger(TestListener.class.getName());

    @Override
    public void onTestStart(ITestResult result) {
        LOGGER.info("========== Starting test: " + result.getMethod().getMethodName() + " ==========");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("========== Test PASSED: " + result.getMethod().getMethodName() + " ==========");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.severe("========== Test FAILED: " + result.getMethod().getMethodName() + " ==========");
        
        Object testInstance = result.getInstance();
        
        // Attempt to get WebDriver instance from test class
        WebDriver driver = getDriverFromTestInstance(testInstance);
        
        if (driver != null) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getMethod().getMethodName());
            LOGGER.info("Screenshot captured at: " + screenshotPath);
            
            // Log failure details
            if (result.getThrowable() != null) {
                LOGGER.severe("Failure reason: " + result.getThrowable().getMessage());
            }
        } else {
            LOGGER.warning("Could not capture screenshot - WebDriver not available");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warning("========== Test SKIPPED: " + result.getMethod().getMethodName() + " ==========");
        if (result.getThrowable() != null) {
            LOGGER.warning("Skip reason: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onStart(ITestContext context) {
        LOGGER.info("========== Starting Test Suite: " + context.getName() + " ==========");
        LOGGER.info("Start time: " + context.getStartDate());
    }

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info("========== Completed Test Suite: " + context.getName() + " ==========");
        LOGGER.info("End time: " + context.getEndDate());
        
        // Log test execution summary
        int total = context.getAllTestMethods().length;
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        
        LOGGER.info(String.format("Total tests: %d, Passed: %d, Failed: %d, Skipped: %d", 
                total, passed, failed, skipped));
    }

    /**
     * Attempts to get the WebDriver instance from the test class
     *
     * @param testInstance The test class instance
     * @return WebDriver instance or null if not available
     */
    private WebDriver getDriverFromTestInstance(Object testInstance) {
        try {
            // Attempt to get driver field through reflection
            java.lang.reflect.Field driverField = testInstance.getClass().getSuperclass().getDeclaredField("driver");
            driverField.setAccessible(true);
            return (WebDriver) driverField.get(testInstance);
        } catch (Exception e) {
            LOGGER.warning("Could not access WebDriver from test class: " + e.getMessage());
            return null;
        }
    }
}