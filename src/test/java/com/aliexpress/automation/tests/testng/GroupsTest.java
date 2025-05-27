package com.aliexpress.automation.tests.testng;

import com.aliexpress.automation.base.BaseTest;
import com.aliexpress.automation.pages.HomePage;
import com.aliexpress.automation.pages.ProductSearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class demonstrating TestNG groups functionality
 */
public class GroupsTest extends BaseTest {

    /**
     * Test search for smartphone (electronics group)
     */
    @Test(groups = {"smoke", "electronics"})
    public void testSmartphoneSearch() {
        LOGGER.info("Starting test: testSmartphoneSearch");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for smartphone
        ProductSearchPage searchPage = homePage.searchProductByCss("smartphone");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " smartphone results");
        
        Assert.assertTrue(resultsCount > 0, "Should find smartphone results");
    }
    
    /**
     * Test search for dress (fashion group)
     */
    @Test(groups = {"regression", "fashion"})
    public void testDressSearch() {
        LOGGER.info("Starting test: testDressSearch");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for dress
        ProductSearchPage searchPage = homePage.searchProductByCss("dress");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " dress results");
        
        Assert.assertTrue(resultsCount > 0, "Should find dress results");
    }
    
    /**
     * Test search for jewelry (fashion group)
     */
    @Test(groups = {"regression", "fashion"})
    public void testJewelrySearch() {
        LOGGER.info("Starting test: testJewelrySearch");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for jewelry
        ProductSearchPage searchPage = homePage.searchProductByCss("jewelry");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " jewelry results");
        
        Assert.assertTrue(resultsCount > 0, "Should find jewelry results");
    }
    
    /**
     * Test search for toys (toys group and smoke group)
     */
    @Test(groups = {"smoke", "toys"})
    public void testToysSearch() {
        LOGGER.info("Starting test: testToysSearch");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for toys
        ProductSearchPage searchPage = homePage.searchProductByCss("toys");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " toys results");
        
        Assert.assertTrue(resultsCount > 0, "Should find toys results");
    }
    
    /**
     * Test search for furniture (home group)
     */
    @Test(groups = {"regression", "home"})
    public void testFurnitureSearch() {
        LOGGER.info("Starting test: testFurnitureSearch");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for furniture
        ProductSearchPage searchPage = homePage.searchProductByCss("furniture");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " furniture results");
        
        Assert.assertTrue(resultsCount > 0, "Should find furniture results");
    }
    
    /**
     * Test search for kitchen (home group and smoke group)
     */
    @Test(groups = {"smoke", "home"})
    public void testKitchenSearch() {
        LOGGER.info("Starting test: testKitchenSearch");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for kitchen
        ProductSearchPage searchPage = homePage.searchProductByCss("kitchen");
        
        // Verify search results
        int resultsCount = searchPage.getSearchResults().size();
        LOGGER.info("Found " + resultsCount + " kitchen results");
        
        Assert.assertTrue(resultsCount > 0, "Should find kitchen results");
    }
}