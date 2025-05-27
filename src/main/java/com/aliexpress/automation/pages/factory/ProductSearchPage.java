package com.aliexpress.automation.pages.factory;

import com.aliexpress.automation.utils.JavaScriptUtils;
import com.aliexpress.automation.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Logger;

/**
 * Page Object for AliExpress Product Search Page using Page Factory pattern
 */
public class ProductSearchPage {
    private static final Logger LOGGER = Logger.getLogger(ProductSearchPage.class.getName());
    private WebDriver driver;
    private WaitUtils waitUtils;
    private JavaScriptUtils jsUtils;

    @FindBy(css = ".product-item, .search-item, .item-card")
    private List<WebElement> searchResults;
    
    @FindBy(css = ".sort-dropdown, .sort-options")
    private WebElement sortDropdown;
    
    @FindBy(xpath = "//a[contains(text(), 'Price: low to high')]")
    private WebElement sortByPriceLowToHigh;
    
    @FindBy(xpath = "//a[contains(text(), 'Price: high to low')]")
    private WebElement sortByPriceHighToLow;
    
    @FindBy(xpath = "//a[contains(text(), 'Best match')]")
    private WebElement sortByBestMatch;
    
    @FindBy(css = "input[placeholder*='min'], input[aria-label*='minimum price']")
    private WebElement minPriceInput;
    
    @FindBy(css = "input[placeholder*='max'], input[aria-label*='maximum price']")
    private WebElement maxPriceInput;
    
    @FindBy(css = ".apply-price, .price-filter-button")
    private WebElement applyPriceButton;
    
    @FindBy(css = ".next-page, a[aria-label='Next Page']")
    private WebElement nextPageButton;
    
    @FindBy(css = ".prev-page, a[aria-label='Previous Page']")
    private WebElement prevPageButton;
    
    @FindBy(css = ".filter-sidebar, .refine-panel")
    private WebElement filterSidebar;
    
    @FindBy(css = ".results-count, .search-count")
    private WebElement resultsCount;

    public ProductSearchPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        this.jsUtils = new JavaScriptUtils(driver);
        PageFactory.initElements(driver, this);
        LOGGER.info("Initialized Product Search Page elements with Page Factory");
    }

    /**
     * Gets list of search results
     *
     * @return List of product WebElements
     */
    public List<WebElement> getSearchResults() {
        waitUtils.waitForPageLoad();
        return searchResults;
    }

    /**
     * Opens a product by index
     *
     * @param index Index of product to open (0-based)
     * @return ProductDetailPage instance
     */
    public ProductDetailPage openProductByIndex(int index) {
        try {
            LOGGER.info("Opening product at index: " + index);
            
            if (index < 0 || index >= searchResults.size()) {
                throw new IndexOutOfBoundsException("Product index out of bounds: " + index);
            }
            
            WebElement product = searchResults.get(index);
            jsUtils.scrollToElement(product);
            product.click();
            
            waitUtils.waitForPageLoad();
            return new ProductDetailPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error opening product: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Sorts products by price (low to high)
     */
    public void sortByPriceLowToHigh() {
        try {
            LOGGER.info("Sorting by price: low to high");
            sortDropdown.click();
            waitUtils.waitForElementToBeClickable(sortByPriceLowToHigh).click();
            waitUtils.waitForPageLoad();
        } catch (Exception e) {
            LOGGER.severe("Error sorting by price low to high: " + e.getMessage());
        }
    }

    /**
     * Sorts products by price (high to low)
     */
    public void sortByPriceHighToLow() {
        try {
            LOGGER.info("Sorting by price: high to low");
            sortDropdown.click();
            waitUtils.waitForElementToBeClickable(sortByPriceHighToLow).click();
            waitUtils.waitForPageLoad();
        } catch (Exception e) {
            LOGGER.severe("Error sorting by price high to low: " + e.getMessage());
        }
    }

    /**
     * Sorts products by best match
     */
    public void sortByBestMatch() {
        try {
            LOGGER.info("Sorting by best match");
            sortDropdown.click();
            waitUtils.waitForElementToBeClickable(sortByBestMatch).click();
            waitUtils.waitForPageLoad();
        } catch (Exception e) {
            LOGGER.severe("Error sorting by best match: " + e.getMessage());
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
            
            minPriceInput.clear();
            minPriceInput.sendKeys(minPrice);
            
            maxPriceInput.clear();
            maxPriceInput.sendKeys(maxPrice);
            
            if (applyPriceButton != null && applyPriceButton.isDisplayed()) {
                applyPriceButton.click();
            } else {
                // Press Enter if no apply button
                maxPriceInput.sendKeys(org.openqa.selenium.Keys.ENTER);
            }
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully set price range");
        } catch (Exception e) {
            LOGGER.severe("Error setting price range: " + e.getMessage());
        }
    }

    /**
     * Goes to next page of search results
     */
    public void goToNextPage() {
        try {
            LOGGER.info("Navigating to next page");
            jsUtils.scrollToElement(nextPageButton);
            nextPageButton.click();
            waitUtils.waitForPageLoad();
        } catch (Exception e) {
            LOGGER.severe("Error navigating to next page: " + e.getMessage());
        }
    }

    /**
     * Goes to previous page of search results
     */
    public void goToPreviousPage() {
        try {
            LOGGER.info("Navigating to previous page");
            jsUtils.scrollToElement(prevPageButton);
            prevPageButton.click();
            waitUtils.waitForPageLoad();
        } catch (Exception e) {
            LOGGER.severe("Error navigating to previous page: " + e.getMessage());
        }
    }

    /**
     * Applies filter by name and value
     *
     * @param filterName  Name of filter (e.g., "Brand", "Color")
     * @param filterValue Value to filter by
     */
    public void applyFilter(String filterName, String filterValue) {
        try {
            LOGGER.info("Applying filter: " + filterName + " = " + filterValue);
            
            // Find filter section by name
            WebElement filterSection = driver.findElement(
                org.openqa.selenium.By.xpath("//div[contains(@class, 'filter-section') and .//span[contains(text(), '" + filterName + "')]]"));
            
            // Expand filter section if collapsed
            try {
                WebElement expandButton = filterSection.findElement(
                    org.openqa.selenium.By.cssSelector(".expand-button, .toggle-icon"));
                if (expandButton.isDisplayed()) {
                    expandButton.click();
                }
            } catch (Exception e) {
                LOGGER.info("Filter section already expanded or no expand button");
            }
            
            // Click on filter value
            WebElement filterValueElement = filterSection.findElement(
                org.openqa.selenium.By.xpath(".//label[contains(text(), '" + filterValue + "') or ./span[contains(text(), '" + filterValue + "')]]"));
            
            jsUtils.scrollToElement(filterValueElement);
            filterValueElement.click();
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully applied filter");
        } catch (Exception e) {
            LOGGER.severe("Error applying filter: " + e.getMessage());
        }
    }

    /**
     * Gets results count
     *
     * @return Count of search results
     */
    public int getResultsCount() {
        return searchResults.size();
    }

    /**
     * Gets results count text from page
     *
     * @return Results count text
     */
    public String getResultsCountText() {
        try {
            return resultsCount.getText();
        } catch (Exception e) {
            LOGGER.warning("Error getting results count text: " + e.getMessage());
            return "";
        }
    }
}