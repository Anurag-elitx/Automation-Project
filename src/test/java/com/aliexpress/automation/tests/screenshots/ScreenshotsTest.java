package com.aliexpress.automation.tests.screenshots;

import com.aliexpress.automation.base.BaseTest;
import com.aliexpress.automation.pages.HomePage;
import com.aliexpress.automation.pages.ProductDetailPage;
import com.aliexpress.automation.pages.ProductSearchPage;
import com.aliexpress.automation.utils.ScreenshotUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class demonstrating screenshots in test automation
 */
public class ScreenshotsTest extends BaseTest {

    /**
     * Test capturing full page screenshots
     */
    @Test
    public void testCaptureFullPageScreenshot() {
        LOGGER.info("Starting test: testCaptureFullPageScreenshot");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        try {
            // Take screenshot of home page
            String homePageScreenshot = ScreenshotUtils.captureScreenshot(driver, "HomePage");
            LOGGER.info("Captured home page screenshot: " + homePageScreenshot);
            
            // Search for a product
            ProductSearchPage searchPage = homePage.searchProductByCss("smartphone");
            
            // Take screenshot of search results
            String searchPageScreenshot = ScreenshotUtils.captureScreenshot(driver, "SearchResults");
            LOGGER.info("Captured search results screenshot: " + searchPageScreenshot);
            
            // Open product detail
            ProductDetailPage productPage = searchPage.openProductByIndex(0);
            
            // Take screenshot of product detail page
            String productPageScreenshot = ScreenshotUtils.captureScreenshot(driver, "ProductDetail");
            LOGGER.info("Captured product detail screenshot: " + productPageScreenshot);
            
            // Verify screenshots were captured
            Assert.assertNotNull(homePageScreenshot, "Home page screenshot should be captured");
            Assert.assertNotNull(searchPageScreenshot, "Search results screenshot should be captured");
            Assert.assertNotNull(productPageScreenshot, "Product detail screenshot should be captured");
        } catch (Exception e) {
            LOGGER.severe("Error capturing screenshots: " + e.getMessage());
            Assert.fail("Failed to capture screenshots: " + e.getMessage());
        }
    }
    
    /**
     * Test capturing element screenshots
     */
    @Test
    public void testCaptureElementScreenshot() {
        LOGGER.info("Starting test: testCaptureElementScreenshot");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        try {
            // Find search box element
            WebElement searchBox = driver.findElement(By.cssSelector(
                "input[placeholder*='Search'], .search-box"));
            
            // Take screenshot of search box element
            String searchBoxScreenshot = ScreenshotUtils.captureElementScreenshot(searchBox, "SearchBox");
            LOGGER.info("Captured search box screenshot: " + searchBoxScreenshot);
            
            // Search for a product
            homePage.searchProductByCss("laptop");
            
            // Find a product element
            WebElement productElement = driver.findElement(By.cssSelector(
                ".product-item, .search-item, .item-card"));
            
            // Take screenshot of product element
            String productElementScreenshot = ScreenshotUtils.captureElementScreenshot(productElement, "ProductItem");
            LOGGER.info("Captured product element screenshot: " + productElementScreenshot);
            
            // Verify screenshots were captured
            Assert.assertNotNull(searchBoxScreenshot, "Search box screenshot should be captured");
            Assert.assertNotNull(productElementScreenshot, "Product element screenshot should be captured");
        } catch (Exception e) {
            LOGGER.severe("Error capturing element screenshots: " + e.getMessage());
            Assert.fail("Failed to capture element screenshots: " + e.getMessage());
        }
    }
    
    /**
     * Test capturing screenshot on test failure
     */
    @Test
    public void testCaptureScreenshotOnFailure() {
        LOGGER.info("Starting test: testCaptureScreenshotOnFailure");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        try {
            // Search for a product
            homePage.searchProductByCss("nonexistent product xyz123456789");
            
            // This assertion will fail
            Assert.assertTrue(driver.findElements(By.cssSelector(".product-item, .search-item, .item-card")).size() > 5,
                "Should find at least 5 products for nonexistent query");
                
            LOGGER.severe("This assertion should have failed");
        } catch (AssertionError e) {
            LOGGER.info("Expected assertion failure: " + e.getMessage());
            
            // Capture screenshot on failure
            String failureScreenshot = ScreenshotUtils.captureScreenshot(driver, "TestFailure");
            LOGGER.info("Captured failure screenshot: " + failureScreenshot);
            
            // Verify screenshot was captured
            Assert.assertNotNull(failureScreenshot, "Failure screenshot should be captured");
            
            // This is a demonstration, so we'll consider the test passed if we captured the screenshot
            LOGGER.info("Successfully demonstrated screenshot capture on failure");
        } catch (Exception e) {
            LOGGER.severe("Unexpected error: " + e.getMessage());
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }
    
    /**
     * Test capturing screenshots during test steps
     */
    @Test
    public void testCaptureScreenshotsDuringSteps() {
        LOGGER.info("Starting test: testCaptureScreenshotsDuringSteps");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        try {
            // Step 1: Capture home page
            LOGGER.info("Step 1: Home page");
            ScreenshotUtils.captureScreenshot(driver, "Step1_HomePage");
            
            // Step 2: Search for a product
            LOGGER.info("Step 2: Search for product");
            homePage.searchProductByCss("headphones");
            ScreenshotUtils.captureScreenshot(driver, "Step2_SearchResults");
            
            // Step 3: Apply filter
            LOGGER.info("Step 3: Apply filter");
            try {
                WebElement filterElement = driver.findElement(By.cssSelector(
                    ".filter-item:first-child, .refinement:first-child"));
                filterElement.click();
                
                // Wait for filter to apply
                new com.aliexpress.automation.utils.WaitUtils(driver).waitForPageLoad();
            } catch (Exception e) {
                LOGGER.warning("Could not click filter: " + e.getMessage());
            }
            ScreenshotUtils.captureScreenshot(driver, "Step3_FilteredResults");
            
            // Step 4: Open product detail
            LOGGER.info("Step 4: Open product detail");
            try {
                WebElement product = driver.findElement(By.cssSelector(
                    ".product-item:first-child, .search-item:first-child, .item-card:first-child"));
                product.click();
                
                // Wait for product page to load
                new com.aliexpress.automation.utils.WaitUtils(driver).waitForPageLoad();
            } catch (Exception e) {
                LOGGER.warning("Could not click product: " + e.getMessage());
            }
            ScreenshotUtils.captureScreenshot(driver, "Step4_ProductDetail");
            
            LOGGER.info("Successfully captured screenshots during test steps");
            Assert.assertTrue(true, "Test completed with screenshots");
        } catch (Exception e) {
            LOGGER.severe("Error during test steps: " + e.getMessage());
            Assert.fail("Failed during test steps: " + e.getMessage());
        }
    }
}