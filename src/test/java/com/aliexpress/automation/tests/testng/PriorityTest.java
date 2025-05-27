package com.aliexpress.automation.tests.testng;

import com.aliexpress.automation.base.BaseTest;
import com.aliexpress.automation.pages.CartPage;
import com.aliexpress.automation.pages.HomePage;
import com.aliexpress.automation.pages.ProductDetailPage;
import com.aliexpress.automation.pages.ProductSearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class demonstrating TestNG test priorities
 */
public class PriorityTest extends BaseTest {

    private static String productName;

    /**
     * First priority test: Verify homepage loaded
     */
    @Test(priority = 1)
    public void verifyHomePageLoaded() {
        LOGGER.info("Starting test: verifyHomePageLoaded (Priority 1)");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should be loaded");
        
        LOGGER.info("Successfully verified homepage is loaded");
    }
    
    /**
     * Second priority test: Search for product
     */
    @Test(priority = 2)
    public void searchForProduct() {
        LOGGER.info("Starting test: searchForProduct (Priority 2)");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for product
        ProductSearchPage searchPage = homePage.searchProductByCss("bluetooth speaker");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " search results");
        
        Assert.assertTrue(resultsCount > 0, "Should find search results");
    }
    
    /**
     * Third priority test: Open product detail
     */
    @Test(priority = 3)
    public void openProductDetail() {
        LOGGER.info("Starting test: openProductDetail (Priority 3)");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for product
        ProductSearchPage searchPage = homePage.searchProductByCss("bluetooth speaker");
        
        // Open first product
        ProductDetailPage productPage = searchPage.openProductByIndex(0);
        
        // Get and store product title for later verification
        productName = productPage.getProductTitle();
        LOGGER.info("Opened product: " + productName);
        
        Assert.assertFalse(productName.isEmpty(), "Product name should not be empty");
    }
    
    /**
     * Fourth priority test: Add to cart
     */
    @Test(priority = 4)
    public void addToCart() {
        LOGGER.info("Starting test: addToCart (Priority 4)");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for product
        ProductSearchPage searchPage = homePage.searchProductByCss("bluetooth speaker");
        
        // Open first product
        ProductDetailPage productPage = searchPage.openProductByIndex(0);
        
        // Add to cart
        CartPage cartPage = productPage.addToCart();
        
        // Verify cart is not empty
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart should not be empty after adding product");
        
        LOGGER.info("Successfully added product to cart");
    }
    
    /**
     * Fifth priority test: Navigate back to home
     */
    @Test(priority = 5)
    public void navigateBackToHome() {
        LOGGER.info("Starting test: navigateBackToHome (Priority 5)");
        
        // Navigate back to home page
        driver.get(configReader.getProperty("base.url", "https://aliexpress.com"));
        
        // Verify home page loaded
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should be loaded");
        
        LOGGER.info("Successfully navigated back to home page");
    }
    
    /**
     * Lowest priority test: Test with negative priority
     */
    @Test(priority = -1)
    public void negativeTest() {
        LOGGER.info("Starting test: negativeTest (Priority -1)");
        LOGGER.info("This test has negative priority and should run first");
        
        // Verify driver is not null
        Assert.assertNotNull(driver, "WebDriver should be initialized");
    }
    
    /**
     * Default priority test: No priority specified
     */
    @Test
    public void defaultPriorityTest() {
        LOGGER.info("Starting test: defaultPriorityTest (Default Priority 0)");
        LOGGER.info("This test has default priority 0");
        
        // Verify current URL contains AliExpress
        Assert.assertTrue(driver.getCurrentUrl().contains("aliexpress"), 
            "Current URL should contain 'aliexpress'");
    }
}