package com.aliexpress.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.logging.Logger;

/**
 * Page Object for AliExpress Category Page
 */
public class CategoryPage extends BasePage {
    private static final Logger LOGGER = Logger.getLogger(CategoryPage.class.getName());

    // XPath locators
    private final By categoryTitleXpath = By.xpath("//h1[contains(@class, 'category-title') or contains(@class, 'category-name')]");
    private final By subcategoriesXpath = By.xpath("//div[contains(@class, 'sub-category') or contains(@class, 'category-item')]//a");
    private final By productItemsXpath = By.xpath("//div[contains(@class, 'product') or contains(@class, 'item-card')]");
    private final By bannerXpath = By.xpath("//div[contains(@class, 'category-banner') or contains(@class, 'top-banner')]");
    private final By filterSidebarXpath = By.xpath("//div[contains(@class, 'filter-sidebar') or contains(@class, 'refine-panel')]");
    private final By sortOptionsXpath = By.xpath("//div[contains(@class, 'sort-options') or contains(@class, 'sort-dropdown')]");

    // CSS selectors
    private final By categoryTitleCss = By.cssSelector("h1.category-title, .category-name");
    private final By subcategoriesCss = By.cssSelector(".sub-category a, .category-item a");
    private final By productItemsCss = By.cssSelector(".product-item, .item-card");
    private final By bannerCss = By.cssSelector(".category-banner, .top-banner");
    private final By filterSidebarCss = By.cssSelector(".filter-sidebar, .refine-panel");
    private final By sortOptionsCss = By.cssSelector(".sort-options, .sort-dropdown");

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Gets category title
     *
     * @return Category title text
     */
    public String getCategoryTitle() {
        try {
            LOGGER.info("Getting category title");
            WebElement titleElement = isElementDisplayed(categoryTitleXpath) ?
                driver.findElement(categoryTitleXpath) :
                driver.findElement(categoryTitleCss);
                
            return getText(titleElement);
        } catch (Exception e) {
            LOGGER.severe("Error getting category title: " + e.getMessage());
            return "";
        }
    }

    /**
     * Gets all subcategories
     *
     * @return List of WebElements representing subcategories
     */
    public List<WebElement> getSubcategories() {
        try {
            LOGGER.info("Getting subcategories");
            waitUtils.waitForPageLoad();
            return driver.findElements(subcategoriesCss);
        } catch (Exception e) {
            LOGGER.severe("Error getting subcategories: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Gets all product items in this category
     *
     * @return List of WebElements representing products
     */
    public List<WebElement> getProductItems() {
        try {
            LOGGER.info("Getting product items");
            waitUtils.waitForPageLoad();
            return driver.findElements(productItemsCss);
        } catch (Exception e) {
            LOGGER.severe("Error getting product items: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Opens subcategory by name
     *
     * @param subcategoryName Name of subcategory to open
     * @return CategoryPage instance for the subcategory
     */
    public CategoryPage openSubcategory(String subcategoryName) {
        try {
            LOGGER.info("Opening subcategory: " + subcategoryName);
            By subcategoryLocator = By.xpath("//a[contains(text(), '" + subcategoryName + "')]");
            WebElement subcategory = waitUtils.waitForElementToBeVisible(subcategoryLocator);
            
            jsUtils.scrollToElement(subcategory);
            click(subcategory);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully opened subcategory: " + subcategoryName);
            return new CategoryPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error opening subcategory: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Opens product by index
     *
     * @param index Index of product to open (0-based)
     * @return ProductDetailPage instance
     */
    public ProductDetailPage openProductByIndex(int index) {
        try {
            LOGGER.info("Opening product at index: " + index);
            List<WebElement> products = getProductItems();
            
            if (index < 0 || index >= products.size()) {
                throw new IndexOutOfBoundsException("Product index out of bounds: " + index);
            }
            
            WebElement product = products.get(index);
            jsUtils.scrollToElement(product);
            click(product);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully opened product at index: " + index);
            return new ProductDetailPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error opening product: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Applies filter from sidebar
     *
     * @param filterName  Name of the filter (e.g., "Brand", "Price")
     * @param filterValue Value to filter by
     */
    public void applyFilter(String filterName, String filterValue) {
        try {
            LOGGER.info("Applying filter: " + filterName + " = " + filterValue);
            
            // Find the filter section
            By filterSectionLocator = By.xpath("//div[contains(@class, 'filter-section') and .//span[contains(text(), '" + filterName + "')]]");
            WebElement filterSection = waitUtils.waitForElementToBeVisible(filterSectionLocator);
            
            // Expand filter section if collapsed
            try {
                WebElement expandButton = filterSection.findElement(By.cssSelector(".expand-button, .toggle-icon"));
                if (expandButton.isDisplayed()) {
                    click(expandButton);
                }
            } catch (Exception e) {
                LOGGER.info("Filter section already expanded or no expand button");
            }
            
            // Click on the specific filter value
            By filterValueLocator = By.xpath(".//label[contains(text(), '" + filterValue + "') or ./span[contains(text(), '" + filterValue + "')]]");
            WebElement filterValueElement = filterSection.findElement(filterValueLocator);
            
            jsUtils.scrollToElement(filterValueElement);
            click(filterValueElement);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully applied filter");
        } catch (Exception e) {
            LOGGER.severe("Error applying filter: " + e.getMessage());
        }
    }

    /**
     * Sorts products by specified option
     *
     * @param sortOption Sort option text (e.g., "Price: low to high", "Popularity")
     */
    public void sortProductsBy(String sortOption) {
        try {
            LOGGER.info("Sorting products by: " + sortOption);
            
            // Click on sort dropdown
            WebElement sortDropdown = isElementDisplayed(sortOptionsXpath) ?
                driver.findElement(sortOptionsXpath) :
                driver.findElement(sortOptionsCss);
            click(sortDropdown);
            
            // Click on specific sort option
            By sortOptionLocator = By.xpath("//a[contains(text(), '" + sortOption + "')]");
            click(sortOptionLocator);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully sorted products by: " + sortOption);
        } catch (Exception e) {
            LOGGER.severe("Error sorting products: " + e.getMessage());
        }
    }

    /**
     * Checks if category page is loaded correctly
     *
     * @return true if category page is loaded, false otherwise
     */
    public boolean isCategoryPageLoaded() {
        try {
            return isElementDisplayed(categoryTitleCss) && 
                   driver.findElements(productItemsCss).size() > 0;
        } catch (Exception e) {
            LOGGER.severe("Error checking if category page is loaded: " + e.getMessage());
            return false;
        }
    }
}