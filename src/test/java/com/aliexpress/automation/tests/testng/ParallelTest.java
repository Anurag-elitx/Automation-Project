package com.aliexpress.automation.tests.testng;

import com.aliexpress.automation.base.BaseTest;
import com.aliexpress.automation.pages.HomePage;
import com.aliexpress.automation.pages.ProductSearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class demonstrating TestNG parallel test execution
 * This class is configured to run tests in parallel in the testng.xml file
 */
public class ParallelTest extends BaseTest {

    /**
     * Test search for smartphones
     */
    @Test(groups = {"search", "electronics"})
    public void testSearchSmartphones() {
        LOGGER.info("Starting test: testSearchSmartphones on thread: " + Thread.currentThread().getId());
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for smartphones
        ProductSearchPage searchPage = homePage.searchProductByCss("smartphone");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " smartphone results on thread: " + Thread.currentThread().getId());
        
        Assert.assertTrue(resultsCount > 0, "Should find smartphone results");
    }
    
    /**
     * Test search for laptops
     */
    @Test(groups = {"search", "electronics", "computers"})
    public void testSearchLaptops() {
        LOGGER.info("Starting test: testSearchLaptops on thread: " + Thread.currentThread().getId());
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for laptops
        ProductSearchPage searchPage = homePage.searchProductByCss("laptop");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " laptop results on thread: " + Thread.currentThread().getId());
        
        Assert.assertTrue(resultsCount > 0, "Should find laptop results");
    }
    
    /**
     * Test search for headphones
     */
    @Test(groups = {"search", "electronics", "audio"})
    public void testSearchHeadphones() {
        LOGGER.info("Starting test: testSearchHeadphones on thread: " + Thread.currentThread().getId());
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for headphones
        ProductSearchPage searchPage = homePage.searchProductByCss("headphones");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " headphone results on thread: " + Thread.currentThread().getId());
        
        Assert.assertTrue(resultsCount > 0, "Should find headphone results");
    }
    
    /**
     * Test search for smart watches
     */
    @Test(groups = {"search", "electronics", "wearables"})
    public void testSearchSmartWatches() {
        LOGGER.info("Starting test: testSearchSmartWatches on thread: " + Thread.currentThread().getId());
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for smart watches
        ProductSearchPage searchPage = homePage.searchProductByCss("smart watch");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " smart watch results on thread: " + Thread.currentThread().getId());
        
        Assert.assertTrue(resultsCount > 0, "Should find smart watch results");
    }
    
    /**
     * Test search for tablets
     */
    @Test(groups = {"search", "electronics", "computers"})
    public void testSearchTablets() {
        LOGGER.info("Starting test: testSearchTablets on thread: " + Thread.currentThread().getId());
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for tablets
        ProductSearchPage searchPage = homePage.searchProductByCss("tablet");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " tablet results on thread: " + Thread.currentThread().getId());
        
        Assert.assertTrue(resultsCount > 0, "Should find tablet results");
    }
}