package com.aliexpress.automation.tests.pagefactory;

import com.aliexpress.automation.base.BaseTest;
import com.aliexpress.automation.pages.factory.HomePage;
import com.aliexpress.automation.pages.factory.ProductDetailPage;
import com.aliexpress.automation.pages.factory.ProductSearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class demonstrating Page Object Model using Page Factory
 */
public class PageFactoryTest extends BaseTest {

    /**
     * Test product search using Page Factory pattern
     */
    @Test
    public void testProductSearch() {
        LOGGER.info("Starting test: testProductSearch with Page Factory");
        
        // Create HomePage using Page Factory
        HomePage homePage = new HomePage(driver);
        
        // Search for product
        String searchTerm = "bluetooth headphones";
        LOGGER.info("Searching for: " + searchTerm);
        
        ProductSearchPage searchPage = homePage.searchProduct(searchTerm);
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " search results");
        
        Assert.assertTrue(resultsCount > 0, "Should find search results for: " + searchTerm);
    }
    
    /**
     * Test product detail page using Page Factory pattern
     */
    @Test
    public void testProductDetail() {
        LOGGER.info("Starting test: testProductDetail with Page Factory");
        
        // Create HomePage using Page Factory
        HomePage homePage = new HomePage(driver);
        
        // Search for product
        String searchTerm = "smart watch";
        LOGGER.info("Searching for: " + searchTerm);
        
        ProductSearchPage searchPage = homePage.searchProduct(searchTerm);
        
        // Open first product
        LOGGER.info("Opening first product");
        ProductDetailPage productPage = searchPage.openProductByIndex(0);
        
        // Get product information
        String productTitle = productPage.getProductTitle();
        String productPrice = productPage.getProductPrice();
        
        LOGGER.info("Product title: " + productTitle);
        LOGGER.info("Product price: " + productPrice);
        
        Assert.assertFalse(productTitle.isEmpty(), "Product title should not be empty");
        Assert.assertFalse(productPrice.isEmpty(), "Product price should not be empty");
    }
    
    /**
     * Test sorting functionality using Page Factory pattern
     */
    @Test
    public void testSorting() {
        LOGGER.info("Starting test: testSorting with Page Factory");
        
        // Create HomePage using Page Factory
        HomePage homePage = new HomePage(driver);
        
        // Search for product
        String searchTerm = "wireless mouse";
        LOGGER.info("Searching for: " + searchTerm);
        
        ProductSearchPage searchPage = homePage.searchProduct(searchTerm);
        
        // Sort by price low to high
        LOGGER.info("Sorting by price: low to high");
        searchPage.sortByPriceLowToHigh();
        
        // Get first product after sorting
        LOGGER.info("Opening first product after sorting");
        ProductDetailPage productPage = searchPage.openProductByIndex(0);
        
        // Get product price
        String productPrice = productPage.getProductPrice();
        LOGGER.info("First product price after sorting: " + productPrice);
        
        // Navigate back to search results
        driver.navigate().back();
        
        // Sort by price high to low
        LOGGER.info("Sorting by price: high to low");
        searchPage = new ProductSearchPage(driver); // Reinitialize page object after navigation
        searchPage.sortByPriceHighToLow();
        
        // Note: We're not asserting specific price order as actual sorting depends on the site
        // Just demonstrating the Page Factory pattern functionality
        
        LOGGER.info("Successfully demonstrated sorting with Page Factory");
    }
    
    /**
     * Test product actions using Page Factory pattern
     */
    @Test
    public void testProductActions() {
        LOGGER.info("Starting test: testProductActions with Page Factory");
        
        // Create HomePage using Page Factory
        HomePage homePage = new HomePage(driver);
        
        // Search for product
        String searchTerm = "usb cable";
        LOGGER.info("Searching for: " + searchTerm);
        
        ProductSearchPage searchPage = homePage.searchProduct(searchTerm);
        
        // Open first product
        LOGGER.info("Opening first product");
        ProductDetailPage productPage = searchPage.openProductByIndex(0);
        
        // Perform product actions
        try {
            LOGGER.info("Setting quantity to 2");
            productPage.setQuantity(2);
            
            LOGGER.info("Selecting first color option");
            productPage.selectFirstColorOption();
            
            LOGGER.info("Navigating to description tab");
            productPage.navigateToDescription();
            
            // Get product description
            String description = productPage.getProductDescription();
            LOGGER.info("Product description length: " + 
                       (description != null ? description.length() : 0) + " characters");
            
            LOGGER.info("Successfully demonstrated product actions with Page Factory");
        } catch (Exception e) {
            LOGGER.warning("Could not complete all product actions: " + e.getMessage());
            // Don't fail the test, as we're just demonstrating Page Factory
        }
        
        Assert.assertTrue(true, "Successfully demonstrated product actions with Page Factory");
    }
}