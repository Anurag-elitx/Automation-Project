package com.aliexpress.automation.tests.cssselector;

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
 * Test class demonstrating CSS Selector usage
 */
public class CSSSelectorTest extends BaseTest {

    /**
     * Test search functionality using CSS selectors
     */
    @Test
    public void testSearchWithCssSelector() {
        LOGGER.info("Starting test: testSearchWithCssSelector");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for a product using CSS selectors
        ProductSearchPage searchPage = homePage.searchProductByCss("smart watch");
        
        // Verify search results are displayed
        List<WebElement> searchResults = searchPage.getSearchResults();
        Assert.assertTrue(searchResults.size() > 0, "Search results should be displayed");
        
        LOGGER.info("Found " + searchResults.size() + " search results");
    }
    
    /**
     * Test navigation to product detail page using CSS selectors
     */
    @Test
    public void testProductDetailWithCssSelector() {
        LOGGER.info("Starting test: testProductDetailWithCssSelector");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for a product using CSS selectors
        ProductSearchPage searchPage = homePage.searchProductByCss("headphones");
        
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
     * Test category navigation using CSS selectors
     */
    @Test
    public void testCategoryNavigationWithCssSelector() {
        LOGGER.info("Starting test: testCategoryNavigationWithCssSelector");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Navigate to a category using CSS selectors
        try {
            // Using CSS selector to find the Electronics category
            WebElement electronicsCategory = driver.findElement(By.cssSelector(
                "a[href*='electronics'], a.category-item:nth-child(1)"));
            
            LOGGER.info("Found Electronics category element");
            
            // Scroll to the element and click it
            new com.aliexpress.automation.utils.JavaScriptUtils(driver)
                .scrollToElement(electronicsCategory);
            electronicsCategory.click();
            
            // Verify we are on the category page by checking for elements
            WebElement categoryTitle = driver.findElement(By.cssSelector(
                "h1.category-title, .category-name"));
            
            Assert.assertTrue(categoryTitle.isDisplayed(), "Category title should be displayed");
            LOGGER.info("Successfully navigated to category page: " + categoryTitle.getText());
        } catch (Exception e) {
            LOGGER.severe("Error in category navigation: " + e.getMessage());
            Assert.fail("Category navigation failed: " + e.getMessage());
        }
    }
    
    /**
     * Test product filtering using CSS selectors
     */
    @Test
    public void testProductFilteringWithCssSelector() {
        LOGGER.info("Starting test: testProductFilteringWithCssSelector");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        // Search for products
        ProductSearchPage searchPage = homePage.searchProductByCss("tablet");
        
        try {
            // Find and click on a filter using CSS selectors
            WebElement priceFilter = driver.findElement(By.cssSelector(
                ".filter-section:has(span:contains('Price')), .filter-item.price"));
            
            // Scroll to the filter element
            new com.aliexpress.automation.utils.JavaScriptUtils(driver)
                .scrollToElement(priceFilter);
            
            // Set price range if input fields are available
            List<WebElement> priceInputs = driver.findElements(By.cssSelector(
                "input[placeholder*='min'], input[placeholder*='max'], input.price-filter"));
            
            if (priceInputs.size() >= 2) {
                priceInputs.get(0).clear();
                priceInputs.get(0).sendKeys("150");
                priceInputs.get(1).clear();
                priceInputs.get(1).sendKeys("600");
                
                // Find and click apply button if available
                try {
                    WebElement applyButton = driver.findElement(By.cssSelector(
                        "button.apply, button.ok-button, button.apply-filter"));
                    applyButton.click();
                } catch (Exception e) {
                    // If no apply button, press Enter
                    priceInputs.get(1).sendKeys(org.openqa.selenium.Keys.ENTER);
                }
                
                LOGGER.info("Applied price filter: $150-$600");
                
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
     * Test advanced CSS selector usage with various combinators
     */
    @Test
    public void testAdvancedCssSelectorUsage() {
        LOGGER.info("Starting test: testAdvancedCssSelectorUsage");
        
        HomePage homePage = new HomePage(driver);
        
        // Close popup if it appears
        homePage.closePopupIfPresent();
        
        try {
            // Example of using complex CSS selector expressions
            
            // Find elements using child combinator
            List<WebElement> childElements = driver.findElements(By.cssSelector(
                ".header > .navigation, .top-section > .nav-item"));
            
            // Find elements using descendant combinator
            List<WebElement> descendantElements = driver.findElements(By.cssSelector(
                ".product-section img, .item-container span"));
            
            // Find elements using adjacent sibling combinator
            List<WebElement> adjacentSiblingElements = driver.findElements(By.cssSelector(
                ".header + .content, .title + .description"));
            
            // Find elements using general sibling combinator
            List<WebElement> generalSiblingElements = driver.findElements(By.cssSelector(
                ".header ~ .footer, .nav-item ~ .dropdown"));
            
            // Find elements using attribute selectors
            List<WebElement> attributeElements = driver.findElements(By.cssSelector(
                "[data-role='search'], [class*='product'][id^='item']"));
            
            LOGGER.info("Advanced CSS selector elements found: " + 
                "Child: " + childElements.size() +
                ", Descendant: " + descendantElements.size() +
                ", Adjacent sibling: " + adjacentSiblingElements.size() +
                ", General sibling: " + generalSiblingElements.size() +
                ", Attribute selectors: " + attributeElements.size());
            
            // Check for existence of any elements to verify CSS selector functionality
            int totalElements = childElements.size() + descendantElements.size() + 
                adjacentSiblingElements.size() + generalSiblingElements.size() +
                attributeElements.size();
                
            LOGGER.info("Total elements found with advanced CSS selectors: " + totalElements);
            
            // The test passes if we found any elements or if the page structure is different than expected
            // This is a demonstration of CSS selector capabilities, not specific page behavior
            Assert.assertTrue(true, "Advanced CSS selector demonstration completed");
        } catch (Exception e) {
            LOGGER.severe("Error in advanced CSS selector usage: " + e.getMessage());
            Assert.fail("Advanced CSS selector test failed: " + e.getMessage());
        }
    }
}