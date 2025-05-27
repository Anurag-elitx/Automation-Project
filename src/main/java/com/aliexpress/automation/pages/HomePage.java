package com.aliexpress.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.logging.Logger;

/**
 * Page Object for AliExpress Home Page
 */
public class HomePage extends BasePage {
    private static final Logger LOGGER = Logger.getLogger(HomePage.class.getName());

    // XPath locators
    private final By searchBoxXpath = By.xpath("//input[@placeholder='Search products' or @data-role='search-box']");
    private final By searchButtonXpath = By.xpath("//input[@type='submit' or contains(@class, 'search-button')]");
    private final By categoriesMenuXpath = By.xpath("//span[contains(text(), 'Categories')]/parent::div");
    private final By popularCategoriesXpath = By.xpath("//div[contains(@class, 'categories-list')]//a");
    private final By loginButtonXpath = By.xpath("//span[text()='Account' or text()='Sign in']/parent::a");
    private final By cartIconXpath = By.xpath("//span[text()='Cart' or contains(@class, 'cart-icon')]/parent::a");
    private final By topBannerXpath = By.xpath("//div[contains(@class, 'banner-slider') or contains(@class, 'top-banner')]");
    private final By closePopupXpath = By.xpath("//img[contains(@class, 'close') or contains(@class, 'btn-close')]");

    // CSS selectors
    private final By searchBoxCss = By.cssSelector("input[placeholder='Search products'], [data-role='search-box']");
    private final By searchButtonCss = By.cssSelector("input[type='submit'], .search-button");
    private final By categoriesMenuCss = By.cssSelector("div.categories-header, div.category-menu");
    private final By popularCategoriesCss = By.cssSelector(".categories-list a, .category-item");
    private final By loginButtonCss = By.cssSelector("a.sign-in, a.login-btn, a:has(span:contains('Account'))");
    private final By cartIconCss = By.cssSelector("a.cart-icon, a:has(span:contains('Cart'))");
    private final By topBannerCss = By.cssSelector(".banner-slider, .top-banner, .main-banner");
    private final By closePopupCss = By.cssSelector("img.close, .btn-close, .close-button");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Performs a search using XPath locators
     *
     * @param searchQuery Search text
     * @return ProductSearchPage instance
     */
    public ProductSearchPage searchProductByXpath(String searchQuery) {
        try {
            LOGGER.info("Performing search using XPath locators with query: " + searchQuery);
            waitUtils.waitForElementToBeVisible(searchBoxXpath);
            type(searchBoxXpath, searchQuery);
            click(searchButtonXpath);
            waitUtils.waitForPageLoad();
            return new ProductSearchPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error searching product by XPath: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Performs a search using CSS selectors
     *
     * @param searchQuery Search text
     * @return ProductSearchPage instance
     */
    public ProductSearchPage searchProductByCss(String searchQuery) {
        try {
            LOGGER.info("Performing search using CSS selectors with query: " + searchQuery);
            waitUtils.waitForElementToBeVisible(searchBoxCss);
            type(searchBoxCss, searchQuery);
            click(searchButtonCss);
            waitUtils.waitForPageLoad();
            return new ProductSearchPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error searching product by CSS: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Clicks on login button
     *
     * @return LoginPage instance
     */
    public LoginPage clickLoginButton() {
        try {
            LOGGER.info("Clicking on login button");
            // Try XPath first, then CSS if XPath fails
            if (isElementDisplayed(loginButtonXpath)) {
                click(loginButtonXpath);
            } else {
                click(loginButtonCss);
            }
            waitUtils.waitForPageLoad();
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
    public CartPage openShoppingCart() {
        try {
            LOGGER.info("Opening shopping cart");
            // Try XPath first, then CSS if XPath fails
            if (isElementDisplayed(cartIconXpath)) {
                click(cartIconXpath);
            } else {
                click(cartIconCss);
            }
            waitUtils.waitForPageLoad();
            return new CartPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error opening shopping cart: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Opens a category from the main menu using Actions class
     *
     * @param categoryName Name of the category to open
     * @return CategoryPage instance
     */
    public CategoryPage openCategory(String categoryName) {
        try {
            LOGGER.info("Opening category: " + categoryName);
            // First hover over the categories menu
            WebElement categoriesMenu = isElementDisplayed(categoriesMenuXpath) ? 
                driver.findElement(categoriesMenuXpath) : 
                driver.findElement(categoriesMenuCss);
            
            hoverOver(categoriesMenu);
            
            // Then click on the specified category
            By categoryLocator = By.xpath("//a[contains(text(), '" + categoryName + "')]");
            click(categoryLocator);
            
            waitUtils.waitForPageLoad();
            return new CategoryPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error opening category: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Closes popup if present
     */
    public void closePopupIfPresent() {
        try {
            // Try XPath first, then CSS if XPath fails
            if (isElementDisplayed(closePopupXpath)) {
                click(closePopupXpath);
                LOGGER.info("Closed popup using XPath");
            } else if (isElementDisplayed(closePopupCss)) {
                click(closePopupCss);
                LOGGER.info("Closed popup using CSS");
            } else {
                LOGGER.info("No popup detected");
            }
        } catch (Exception e) {
            LOGGER.warning("Error handling popup: " + e.getMessage());
        }
    }

    /**
     * Scrolls through banner images using JavaScriptExecutor
     */
    public void scrollThroughBanners() {
        try {
            LOGGER.info("Scrolling through banners");
            WebElement banner = isElementDisplayed(topBannerXpath) ?
                driver.findElement(topBannerXpath) :
                driver.findElement(topBannerCss);
                
            jsUtils.scrollToElement(banner);
            
            // Simulate banner interactions
            jsUtils.executeScript("arguments[0].scrollLeft += 300", banner);
            Thread.sleep(1000);
            jsUtils.executeScript("arguments[0].scrollLeft += 300", banner);
            
            LOGGER.info("Successfully scrolled through banners");
        } catch (Exception e) {
            LOGGER.warning("Error scrolling through banners: " + e.getMessage());
        }
    }

    /**
     * Gets popular categories from home page
     *
     * @return List of popular category names
     */
    public List<WebElement> getPopularCategories() {
        try {
            LOGGER.info("Getting popular categories");
            return driver.findElements(popularCategoriesCss);
        } catch (Exception e) {
            LOGGER.severe("Error getting popular categories: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Switches to language selection
     *
     * @param language Language to select (e.g., "English")
     */
    public void switchLanguage(String language) {
        try {
            LOGGER.info("Switching language to: " + language);
            By languageSelector = By.xpath("//div[contains(@class, 'language-selector') or contains(@class, 'lang-dropdown')]");
            click(languageSelector);
            
            By languageOption = By.xpath("//a[contains(text(), '" + language + "')]");
            click(languageOption);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully switched language to: " + language);
        } catch (Exception e) {
            LOGGER.severe("Error switching language: " + e.getMessage());
        }
    }

    /**
     * Verifies if home page is loaded successfully
     *
     * @return true if home page is loaded, false otherwise
     */
    public boolean isHomePageLoaded() {
        try {
            return isElementDisplayed(searchBoxCss) && 
                   isElementDisplayed(categoriesMenuCss) &&
                   driver.getTitle().contains("AliExpress");
        } catch (Exception e) {
            LOGGER.severe("Error verifying home page: " + e.getMessage());
            return false;
        }
    }
}