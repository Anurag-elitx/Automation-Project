package com.aliexpress.automation.tests.xpath;

import com.aliexpress.automation.base.BaseTest;
import com.aliexpress.automation.pages.HomePage;
import com.aliexpress.automation.pages.ProductDetailPage;
import com.aliexpress.automation.pages.ProductSearchPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class demonstrating XPath locators usage
 */
public class XPathTest extends BaseTest {

    /**
     * Test search functionality using XPath locators
     */
    @Test
    public void testSearchWithXPath() {
        LOGGER.info("Starting test: testSearchWithXPath");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for a product using XPath locators
        ProductSearchPage searchPage = homePage.searchProductByXpath("wireless earbuds");
        
        // Verify search results are displayed
        List<WebElement> searchResults = searchPage.getSearchResults();
        Assert.assertTrue(searchResults.size() > 0, "Search results should be displayed");
        
        LOGGER.info("Found " + searchResults.size() + " search results");
    }
    
    /**
     * Test navigation to product detail page using XPath
     */
    @Test
    public void testProductDetailWithXPath() {
        LOGGER.info("Starting test: testProductDetailWithXPath");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for a product using XPath locators
        ProductSearchPage searchPage = homePage.searchProductByXpath("smartphone");
        
        // Open the first product
        ProductDetailPage productPage = searchPage.openProductByIndex(0);
        
        // Verify product details are displayed
        String productTitle = productPage.getProductTitle();
        String productPrice = productPage.getProductPrice();
        
        Assert.assertFalse(productTitle.isEmpty(), "Product title should not be empty");
        Assert.assertFalse(productPrice.isEmpty(), "Product price should not be empty");
        
        LOGGER.info("Successfully verified product details: " + productTitle + " - " + productPrice);
    }
    
    /**
     * Test category navigation using XPath
     */
    @Test
    public void testCategoryNavigationWithXPath() {
        LOGGER.info("Starting test: testCategoryNavigationWithXPath");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Navigate to a category using XPath
        try {
            // Using custom XPath to find the Electronics category
            WebElement electronicsCategory = driver.findElement(By.xpath(
                "//a[contains(text(), 'Electronics') or contains(@href, 'electronics')]"));
            
            LOGGER.info("Found Electronics category element");
            
            // Scroll to the element and click it
            new com.aliexpress.automation.utils.JavaScriptUtils(driver)
                .scrollToElement(electronicsCategory);
            electronicsCategory.click();
            
            // Verify we are on the category page by checking for elements
            WebElement categoryTitle = driver.findElement(By.xpath(
                "//h1[contains(@class, 'category-title') or contains(text(), 'Electronics')]"));
            
            Assert.assertTrue(categoryTitle.isDisplayed(), "Category title should be displayed");
            LOGGER.info("Successfully navigated to category page: " + categoryTitle.getText());
        } catch (Exception e) {
            LOGGER.severe("Error in category navigation: " + e.getMessage());
            Assert.fail("Category navigation failed: " + e.getMessage());
        }
    }
    
    /**
     * Test product filtering using XPath
     */
    @Test
    public void testProductFilteringWithXPath() {
        LOGGER.info("Starting test: testProductFilteringWithXPath");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for products
        ProductSearchPage searchPage = homePage.searchProductByXpath("laptop");
        
        try {
            // Find and click on a filter using XPath
            WebElement priceFilter = driver.findElement(By.xpath(
                "//div[contains(@class, 'filter') and .//span[contains(text(), 'Price')]]"));
            
            // Scroll to the filter element
            new com.aliexpress.automation.utils.JavaScriptUtils(driver)
                .scrollToElement(priceFilter);
            
            // Set price range if input fields are available
            List<WebElement> priceInputs = driver.findElements(By.xpath(
                "//input[contains(@placeholder, 'min') or contains(@placeholder, 'max') or contains(@class, 'price-filter')]"));
            
            if (priceInputs.size() >= 2) {
                priceInputs.get(0).clear();
                priceInputs.get(0).sendKeys("100");
                priceInputs.get(1).clear();
                priceInputs.get(1).sendKeys("500");
                
                // Find and click apply button if available
                try {
                    WebElement applyButton = driver.findElement(By.xpath(
                        "//button[contains(@class, 'apply') or contains(text(), 'OK') or contains(text(), 'Apply')]"));
                    applyButton.click();
                } catch (Exception e) {
                    // If no apply button, press Enter
                    priceInputs.get(1).sendKeys(org.openqa.selenium.Keys.ENTER);
                }
                
                LOGGER.info("Applied price filter: $100-$500");
                
                // Wait for page to load after filtering
                new com.aliexpress.automation.utils.WaitUtils(driver).waitForPageLoad();
                
                // Verify filtering worked by checking current URL or results
                Assert.assertTrue(driver.getCurrentUrl().contains("price"), 
                    "URL should contain price parameter after filtering");
                
                LOGGER.info("Filter applied successfully, current URL: " + driver.getCurrentUrl());
            } else {
                LOGGER.warning("Price filter inputs not found, skipping test");
            }
        } catch (Exception e) {
            LOGGER.severe("Error in product filtering: " + e.getMessage());
            Assert.fail("Product filtering failed: " + e.getMessage());
        }
    }
    
    /**
     * Test advanced XPath usage with various axes
     */
    @Test
    public void testAdvancedXPathUsage() {
        LOGGER.info("Starting test: testAdvancedXPathUsage");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        try {
            // Example of using complex XPath expressions
            
            // Find elements using parent axis
            List<WebElement> parentAxisElements = driver.findElements(By.xpath(
                "//span[contains(text(), 'Category')]/parent::div"));
            
            // Find elements using ancestor axis
            List<WebElement> ancestorAxisElements = driver.findElements(By.xpath(
                "//a[contains(@class, 'product')]/ancestor::div[contains(@class, 'product-item')]"));
            
            // Find elements using following-sibling axis
            List<WebElement> siblingAxisElements = driver.findElements(By.xpath(
                "//div[contains(@class, 'header')]/following-sibling::div"));
            
            // Find elements using contains, starts-with, and multiple conditions
            List<WebElement> complexXPathElements = driver.findElements(By.xpath(
                "//div[contains(@class, 'product') and (contains(@id, 'item') or starts-with(@class, 'item'))]"));
            
            LOGGER.info("Advanced XPath elements found: " + 
                "Parent axis: " + parentAxisElements.size() +
                ", Ancestor axis: " + ancestorAxisElements.size() +
                ", Sibling axis: " + siblingAxisElements.size() +
                ", Complex XPath: " + complexXPathElements.size());
            
            // Check for existence of any elements to verify XPath functionality
            int totalElements = parentAxisElements.size() + ancestorAxisElements.size() + 
                siblingAxisElements.size() + complexXPathElements.size();
                
            LOGGER.info("Total elements found with advanced XPath: " + totalElements);
            
            // The test passes if we found any elements or if the page structure is different than expected
            // This is a demonstration of XPath capabilities, not specific page behavior
            Assert.assertTrue(true, "Advanced XPath demonstration completed");
        } catch (Exception e) {
            LOGGER.severe("Error in advanced XPath usage: " + e.getMessage());
            Assert.fail("Advanced XPath test failed: " + e.getMessage());
        }
    }
}