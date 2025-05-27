package com.aliexpress.automation.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Utility class for capturing screenshots during test execution
 */
public class ScreenshotUtils {
    private static final Logger LOGGER = Logger.getLogger(ScreenshotUtils.class.getName());
    private static final String SCREENSHOT_DIRECTORY = "test-output/screenshots/";

    static {
        createScreenshotDirectory();
    }

    /**
     * Creates the screenshot directory if it doesn't exist
     */
    private static void createScreenshotDirectory() {
        File directory = new File(SCREENSHOT_DIRECTORY);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                LOGGER.info("Created screenshot directory at: " + SCREENSHOT_DIRECTORY);
            } else {
                LOGGER.warning("Failed to create screenshot directory");
            }
        }
    }

    /**
     * Captures screenshot of the entire page
     *
     * @param driver    WebDriver instance
     * @param testName  Name of the test
     * @return          Path to the captured screenshot
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            LOGGER.warning("WebDriver is null, cannot capture screenshot");
            return null;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = SCREENSHOT_DIRECTORY + fileName;

        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);
            LOGGER.info("Screenshot captured: " + filePath);
            return filePath;
        } catch (IOException e) {
            LOGGER.severe("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Captures screenshot of a specific WebElement
     *
     * @param element   WebElement to capture
     * @param testName  Name of the test
     * @return          Path to the captured screenshot
     */
    public static String captureElementScreenshot(WebElement element, String testName) {
        if (element == null) {
            LOGGER.warning("WebElement is null, cannot capture screenshot");
            return null;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_element_" + timestamp + ".png";
        String filePath = SCREENSHOT_DIRECTORY + fileName;

        try {
            File srcFile = element.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);
            LOGGER.info("Element screenshot captured: " + filePath);
            return filePath;
        } catch (IOException e) {
            LOGGER.severe("Failed to capture element screenshot: " + e.getMessage());
            return null;
        }
    }
}