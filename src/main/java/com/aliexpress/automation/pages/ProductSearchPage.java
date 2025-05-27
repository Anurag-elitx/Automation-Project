package com.aliexpress.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

/**
 * Page Object for AliExpress Product Search Page
 */
public class ProductSearchPage extends BasePage {
    private static final Logger LOGGER = Logger.getLogger(ProductSearchPage.class.getName());

    // XPath locators
    private final By searchResultsXpath = By.xpath("//div[contains(@class, 'product') or contains(@class, 'item')]");
    private final By sortByDropdownXpath = By.xpath("//span[contains(text(), 'Sort by')]/parent::div");
    private final By filterPanelXpath = By.xpath("//div[contains(@class, 'filter') or contains(@class, 'refinements')]");
    private final By priceSliderXpath = By.xpath("//div[contains(@class, 'price-slider')]//span[contains(@class, 'slider-handle')]");
    private final By priceMinInputXpath = By.xpath("//input[contains(@placeholder, 'min') or contains(@aria-label, 'minimum price')]");
    private final By priceMaxInputXpath = By.xpath("//input[contains(@placeholder, 'max') or contains(@aria-label, 'maximum price')]");
    private final By paginationXpath = By.xpath("//div[contains(@class, 'pagination')]//a");
    private final By nextPageXpath = By.xpath("//a[contains(@class, 'next-page') or contains(text(), 'Next')]");

    // CSS selectors
    private final By searchResultsCss = By.cssSelector(".product-item, .search-item, .item-card");
    private final By sortByDropdownCss = By.cssSelector(".sort-dropdown, .sort-options");
    private final By filterPanelCss = By.cssSelector(".filter-panel, .refinements");
    private final By priceSliderCss = By.cssSelector(".price-slider .slider-handle");
    private final By priceMinInputCss = By.cssSelector("input[placeholder*='min'], input[aria-label*='minimum price']");
    private final By priceMaxInputCss = By.cssSelector("input[placeholder*='max'], input[aria-label*='maximum price']");
    private final By paginationCss = By.cssSelector(".pagination a");
    private final By nextPageCss = By.cssSelector(".next-page, a[aria-label='Next Page']");

    public ProductSearchPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Gets all product elements from search results
     *
     * @return List of WebElements representing products
     */
    public List<WebElement> getSearchResults() {
        try {
            LOGGER.info("Getting search results");
            waitUtils.waitForPageLoad();
            return driver.findElements(searchResultsCss);
        } catch (Exception e) {
            LOGGER.severe("Error getting search results: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Opens a product by index from search results
     *
     * @param index Index of the product to open (0-based)
     * @return ProductDetailPage instance
     */
    public ProductDetailPage openProductByIndex(int index) {
        try {
            LOGGER.info("Opening product at index: " + index);
            List<WebElement> products = getSearchResults();
            
            if (index < 0 || index >= products.size()) {
                throw new IndexOutOfBoundsException("Product index out of bounds: " + index);
            }
            
            WebElement product = products.get(index);
            jsUtils.scrollToElement(product);
            click(product);
            
            waitUtils.waitForPageLoad();
            return new ProductDetailPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error opening product: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Selects sort option from dropdown
     *
     * @param option Sort option (e.g., "Price: low to high", "Best match")
     */
    public void sortBy(String option) {
        try {
            LOGGER.info("Sorting by: " + option);
            // First click the sort dropdown
            WebElement sortDropdown = isElementDisplayed(sortByDropdownXpath) ?
                driver.findElement(sortByDropdownXpath) :
                driver.findElement(sortByDropdownCss);
            
            click(sortDropdown);
            
            // Then select the option
            By optionLocator = By.xpath("//a[contains(text(), '" + option + "')]");
            click(optionLocator);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully sorted by: " + option);
        } catch (Exception e) {
            LOGGER.severe("Error sorting results: " + e.getMessage());
        }
    }

    /**
     * Sets price range filter
     *
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     */
    public void setPriceRange(String minPrice, String maxPrice) {
        try {
            LOGGER.info("Setting price range: " + minPrice + " - " + maxPrice);
            
            // Find the min and max price inputs
            WebElement minInput = isElementDisplayed(priceMinInputXpath) ?
                driver.findElement(priceMinInputXpath) :
                driver.findElement(priceMinInputCss);
                
            WebElement maxInput = isElementDisplayed(priceMaxInputXpath) ?
                driver.findElement(priceMaxInputXpath) :
                driver.findElement(priceMaxInputCss);
            
            // Clear and set values
            type(minInput, minPrice);
            type(maxInput, maxPrice);
            
            // Submit by pressing Enter on the max input
            maxInput.sendKeys(Keys.ENTER);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Price range set successfully");
        } catch (Exception e) {
            LOGGER.severe("Error setting price range: " + e.getMessage());
        }
    }

    /**
     * Navigates to the next page of search results
     */
    public void goToNextPage() {
        try {
            LOGGER.info("Navigating to next page");
            WebElement nextPageButton = isElementDisplayed(nextPageXpath) ?
                driver.findElement(nextPageXpath) :
                driver.findElement(nextPageCss);
            
            jsUtils.scrollToElement(nextPageButton);
            click(nextPageButton);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully navigated to next page");
        } catch (Exception e) {
            LOGGER.severe("Error navigating to next page: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Navigates to a specific page number
     *
     * @param pageNumber Page number to navigate to
     */
    public void goToPage(int pageNumber) {
        try {
            LOGGER.info("Navigating to page: " + pageNumber);
            By pageLocator = By.xpath("//div[contains(@class, 'pagination')]//a[text()='" + pageNumber + "']");
            
            WebElement pageLink = waitUtils.waitForElementToBeClickable(pageLocator);
            jsUtils.scrollToElement(pageLink);
            click(pageLink);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully navigated to page: " + pageNumber);
        } catch (Exception e) {
            LOGGER.severe("Error navigating to page " + pageNumber + ": " + e.getMessage());
        }
    }

    /**
     * Applies filter by specified attribute and value
     *
     * @param filterName  Filter name (e.g., "Brand", "Color")
     * @param filterValue Value to filter by
     */
    public void applyFilter(String filterName, String filterValue) {
        try {
            LOGGER.info("Applying filter: " + filterName + " = " + filterValue);
            
            // Find the filter section by name
            By filterSectionLocator = By.xpath("//div[contains(@class, 'filter-section') and .//span[contains(text(), '" + filterName + "')]]");
            WebElement filterSection = waitUtils.waitForElementToBeVisible(filterSectionLocator);
            
            // Expand filter section if collapsed
            if (isElementDisplayed(By.xpath(".//span[contains(@class, 'expand')]"), filterSection)) {
                click(By.xpath(".//span[contains(@class, 'expand')]"), filterSection);
            }
            
            // Click on the specific filter value
            By filterValueLocator = By.xpath(".//label[contains(text(), '" + filterValue + "') or ./span[contains(text(), '" + filterValue + "')]]");
            WebElement filterValueElement = waitUtils.waitForElementToBeVisible(filterValueLocator, filterSection);
            
            jsUtils.scrollToElement(filterValueElement);
            click(filterValueElement);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully applied filter");
        } catch (Exception e) {
            LOGGER.severe("Error applying filter: " + e.getMessage());
        }
    }

    /**
     * Finds element by XPath within the specified parent element
     *
     * @param xpathLocator XPath locator to find element
     * @param parent Parent WebElement to search within
     * @return WebElement found, or null if not found
     */
    private boolean isElementDisplayed(By locator, WebElement parent) {
        try {
            return parent.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Gets total number of search results
     *
     * @return Count of search results
     */
    public int getResultsCount() {
        try {
            return getSearchResults().size();
        } catch (Exception e) {
            LOGGER.severe("Error getting results count: " + e.getMessage());
            return 0;
        }
    }
}