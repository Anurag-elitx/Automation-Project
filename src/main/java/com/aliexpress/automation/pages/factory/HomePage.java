package com.aliexpress.automation.pages.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Logger;

/**
 * Page Object for AliExpress Home Page using Page Factory pattern
 */
public class HomePage {
    private static final Logger LOGGER = Logger.getLogger(HomePage.class.getName());
    private WebDriver driver;

    @FindBy(xpath = "//input[@placeholder='Search products' or @data-role='search-box']")
    private WebElement searchBox;
    
    @FindBy(css = "input[type='submit'], .search-button")
    private WebElement searchButton;
    
    @FindBy(css = ".categories-list a, .category-item")
    private List<WebElement> popularCategories;
    
    @FindBy(xpath = "//span[text()='Account' or text()='Sign in']/parent::a")
    private WebElement loginButton;
    
    @FindBy(xpath = "//span[text()='Cart' or contains(@class, 'cart-icon')]/parent::a")
    private WebElement cartIcon;
    
    @FindBy(css = ".banner-slider, .top-banner, .main-banner")
    private WebElement topBanner;
    
    @FindBy(css = ".language-selector, .lang-dropdown")
    private WebElement languageSelector;
    
    @FindBy(xpath = "//div[contains(@class, 'close-popup') or contains(@class, 'btn-close')]")
    private WebElement closePopupButton;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        LOGGER.info("Initialized Home Page elements with Page Factory");
    }

    /**
     * Performs a search for products
     *
     * @param searchQuery Search query text
     * @return ProductSearchPage instance
     */
    public ProductSearchPage searchProduct(String searchQuery) {
        try {
            LOGGER.info("Searching for: " + searchQuery);
            searchBox.clear();
            searchBox.sendKeys(searchQuery);
            searchButton.click();
            return new ProductSearchPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error searching for product: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Clicks on login button
     *
     * @return LoginPage instance
     */
    public LoginPage clickLogin() {
        try {
            LOGGER.info("Clicking login button");
            loginButton.click();
            return new LoginPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error clicking login button: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Opens shopping cart
     *
     * @return CartPage instance
     */
    public CartPage openCart() {
        try {
            LOGGER.info("Opening shopping cart");
            cartIcon.click();
            return new CartPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error opening shopping cart: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Clicks on a category by index
     *
     * @param index Index of category (0-based)
     * @return CategoryPage instance
     */
    public CategoryPage clickCategoryByIndex(int index) {
        try {
            LOGGER.info("Clicking category at index: " + index);
            if (index < 0 || index >= popularCategories.size()) {
                throw new IndexOutOfBoundsException("Category index out of bounds: " + index);
            }
            popularCategories.get(index).click();
            return new CategoryPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error clicking category: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Closes popup if present
     */
    public void closePopupIfPresent() {
        try {
            if (closePopupButton.isDisplayed()) {
                LOGGER.info("Closing popup");
                closePopupButton.click();
            }
        } catch (Exception e) {
            LOGGER.info("No popup present or error closing popup: " + e.getMessage());
        }
    }

    /**
     * Gets all categories
     *
     * @return List of category WebElements
     */
    public List<WebElement> getCategories() {
        return popularCategories;
    }

    /**
     * Selects language
     *
     * @param language Language to select
     */
    public void selectLanguage(String language) {
        try {
            LOGGER.info("Selecting language: " + language);
            languageSelector.click();
            
            // Find and click the specific language option
            WebElement languageOption = driver.findElement(
                org.openqa.selenium.By.xpath("//a[contains(text(), '" + language + "')]"));
            languageOption.click();
            
            LOGGER.info("Successfully selected language: " + language);
        } catch (Exception e) {
            LOGGER.severe("Error selecting language: " + e.getMessage());
        }
    }

    /**
     * Gets page title
     *
     * @return Page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
}