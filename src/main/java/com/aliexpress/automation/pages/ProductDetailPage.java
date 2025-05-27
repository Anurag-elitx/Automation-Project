package com.aliexpress.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.logging.Logger;

/**
 * Page Object for AliExpress Product Detail Page
 */
public class ProductDetailPage extends BasePage {
    private static final Logger LOGGER = Logger.getLogger(ProductDetailPage.class.getName());

    // XPath locators
    private final By productTitleXpath = By.xpath("//h1[contains(@class, 'product-title')]");
    private final By productPriceXpath = By.xpath("//div[contains(@class, 'product-price')]//span[contains(@class, 'price')]");
    private final By addToCartButtonXpath = By.xpath("//button[contains(@class, 'add-to-cart') or contains(text(), 'Add to Cart')]");
    private final By buyNowButtonXpath = By.xpath("//button[contains(@class, 'buy-now') or contains(text(), 'Buy Now')]");
    private final By productImagesXpath = By.xpath("//div[contains(@class, 'product-gallery')]//img");
    private final By colorOptionsXpath = By.xpath("//div[contains(@class, 'sku-property') and .//span[contains(text(), 'Color')]]//li");
    private final By sizeOptionsXpath = By.xpath("//div[contains(@class, 'sku-property') and .//span[contains(text(), 'Size')]]//li");
    private final By quantityInputXpath = By.xpath("//input[@type='number' or contains(@class, 'quantity-input')]");
    private final By quantityIncreaseXpath = By.xpath("//button[contains(@class, 'quantity-increase') or contains(@class, 'increase-btn')]");
    private final By quantityDecreaseXpath = By.xpath("//button[contains(@class, 'quantity-decrease') or contains(@class, 'decrease-btn')]");
    private final By productDescriptionXpath = By.xpath("//div[contains(@class, 'product-description') or contains(@id, 'description')]");
    private final By productSpecificationsXpath = By.xpath("//div[contains(@class, 'specifications') or contains(@class, 'product-specs')]");
    private final By sellerInfoXpath = By.xpath("//div[contains(@class, 'seller-info') or contains(@class, 'store-info')]");
    private final By reviewsTabXpath = By.xpath("//div[contains(@class, 'tab') and .//span[contains(text(), 'Reviews')]]");

    // CSS selectors
    private final By productTitleCss = By.cssSelector("h1.product-title, .product-name");
    private final By productPriceCss = By.cssSelector(".product-price .price, .price-current");
    private final By addToCartButtonCss = By.cssSelector("button.add-to-cart, .add-cart-btn");
    private final By buyNowButtonCss = By.cssSelector("button.buy-now, .buy-now-btn");
    private final By productImagesCss = By.cssSelector(".product-gallery img, .gallery-image");
    private final By colorOptionsCss = By.cssSelector(".sku-property.color .sku-property-item, .color-options .option-item");
    private final By sizeOptionsCss = By.cssSelector(".sku-property.size .sku-property-item, .size-options .option-item");
    private final By quantityInputCss = By.cssSelector("input.quantity-input, input[type='number']");
    private final By quantityIncreaseCss = By.cssSelector(".quantity-increase, .increase-btn");
    private final By quantityDecreaseCss = By.cssSelector(".quantity-decrease, .decrease-btn");
    private final By productDescriptionCss = By.cssSelector(".product-description, #description");
    private final By productSpecificationsCss = By.cssSelector(".specifications, .product-specs");
    private final By sellerInfoCss = By.cssSelector(".seller-info, .store-info");
    private final By reviewsTabCss = By.cssSelector(".tab-reviews, .reviews-tab");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Gets product title
     *
     * @return Product title text
     */
    public String getProductTitle() {
        try {
            LOGGER.info("Getting product title");
            WebElement titleElement = isElementDisplayed(productTitleXpath) ? 
                driver.findElement(productTitleXpath) : 
                driver.findElement(productTitleCss);
                
            return getText(titleElement);
        } catch (Exception e) {
            LOGGER.severe("Error getting product title: " + e.getMessage());
            return "";
        }
    }

    /**
     * Gets product price
     *
     * @return Product price text
     */
    public String getProductPrice() {
        try {
            LOGGER.info("Getting product price");
            WebElement priceElement = isElementDisplayed(productPriceXpath) ? 
                driver.findElement(productPriceXpath) : 
                driver.findElement(productPriceCss);
                
            return getText(priceElement);
        } catch (Exception e) {
            LOGGER.severe("Error getting product price: " + e.getMessage());
            return "";
        }
    }

    /**
     * Adds the product to cart
     *
     * @return CartPage instance
     */
    public CartPage addToCart() {
        try {
            LOGGER.info("Adding product to cart");
            // Select first available color and size if present
            selectFirstColorOption();
            selectFirstSizeOption();
            
            // Click the Add to Cart button
            WebElement addToCartButton = isElementDisplayed(addToCartButtonXpath) ?
                driver.findElement(addToCartButtonXpath) :
                driver.findElement(addToCartButtonCss);
                
            jsUtils.scrollToElement(addToCartButton);
            click(addToCartButton);
            
            // Wait for cart confirmation
            waitUtils.waitForPageLoad();
            
            LOGGER.info("Successfully added product to cart");
            return new CartPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error adding product to cart: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Selects first available color option
     */
    public void selectFirstColorOption() {
        try {
            List<WebElement> colorOptions = isElementDisplayed(colorOptionsXpath) ?
                driver.findElements(colorOptionsXpath) :
                driver.findElements(colorOptionsCss);
                
            if (!colorOptions.isEmpty()) {
                LOGGER.info("Selecting first color option");
                WebElement firstColor = colorOptions.get(0);
                jsUtils.scrollToElement(firstColor);
                click(firstColor);
            } else {
                LOGGER.info("No color options available");
            }
        } catch (Exception e) {
            LOGGER.warning("Error selecting color option: " + e.getMessage());
        }
    }

    /**
     * Selects first available size option
     */
    public void selectFirstSizeOption() {
        try {
            List<WebElement> sizeOptions = isElementDisplayed(sizeOptionsXpath) ?
                driver.findElements(sizeOptionsXpath) :
                driver.findElements(sizeOptionsCss);
                
            if (!sizeOptions.isEmpty()) {
                LOGGER.info("Selecting first size option");
                WebElement firstSize = sizeOptions.get(0);
                jsUtils.scrollToElement(firstSize);
                click(firstSize);
            } else {
                LOGGER.info("No size options available");
            }
        } catch (Exception e) {
            LOGGER.warning("Error selecting size option: " + e.getMessage());
        }
    }

    /**
     * Sets product quantity
     *
     * @param quantity Quantity to set
     */
    public void setQuantity(int quantity) {
        try {
            LOGGER.info("Setting quantity to: " + quantity);
            WebElement quantityInput = isElementDisplayed(quantityInputXpath) ?
                driver.findElement(quantityInputXpath) :
                driver.findElement(quantityInputCss);
                
            WebElement increaseBtn = isElementDisplayed(quantityIncreaseXpath) ?
                driver.findElement(quantityIncreaseXpath) :
                driver.findElement(quantityIncreaseCss);
                
            WebElement decreaseBtn = isElementDisplayed(quantityDecreaseXpath) ?
                driver.findElement(quantityDecreaseXpath) :
                driver.findElement(quantityDecreaseCss);
            
            // Clear and type directly if input is accessible
            try {
                type(quantityInput, String.valueOf(quantity));
            } catch (Exception e) {
                // If direct input fails, use +/- buttons
                LOGGER.info("Using +/- buttons to set quantity");
                // First reset to 1 by clicking decrease multiple times
                for (int i = 0; i < 10; i++) {
                    click(decreaseBtn);
                    Thread.sleep(100);
                }
                
                // Then click increase to desired quantity
                for (int i = 1; i < quantity; i++) {
                    click(increaseBtn);
                    Thread.sleep(100);
                }
            }
            
            LOGGER.info("Successfully set quantity");
        } catch (Exception e) {
            LOGGER.severe("Error setting quantity: " + e.getMessage());
        }
    }

    /**
     * Navigates to product description tab
     */
    public void navigateToDescription() {
        try {
            LOGGER.info("Navigating to product description");
            WebElement descriptionTab = driver.findElement(By.xpath("//div[contains(@class, 'tab') and .//span[contains(text(), 'Description')]]"));
            jsUtils.scrollToElement(descriptionTab);
            click(descriptionTab);
            
            WebElement description = isElementDisplayed(productDescriptionXpath) ?
                driver.findElement(productDescriptionXpath) :
                driver.findElement(productDescriptionCss);
                
            jsUtils.scrollToElement(description);
            LOGGER.info("Successfully navigated to description tab");
        } catch (Exception e) {
            LOGGER.warning("Error navigating to description: " + e.getMessage());
        }
    }

    /**
     * Navigates to product reviews tab
     */
    public void navigateToReviews() {
        try {
            LOGGER.info("Navigating to product reviews");
            WebElement reviewsTab = isElementDisplayed(reviewsTabXpath) ?
                driver.findElement(reviewsTabXpath) :
                driver.findElement(reviewsTabCss);
                
            jsUtils.scrollToElement(reviewsTab);
            click(reviewsTab);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully navigated to reviews tab");
        } catch (Exception e) {
            LOGGER.warning("Error navigating to reviews: " + e.getMessage());
        }
    }

    /**
     * Gets product specification details
     *
     * @return Text of product specifications
     */
    public String getProductSpecifications() {
        try {
            LOGGER.info("Getting product specifications");
            WebElement specifications = isElementDisplayed(productSpecificationsXpath) ?
                driver.findElement(productSpecificationsXpath) :
                driver.findElement(productSpecificationsCss);
                
            jsUtils.scrollToElement(specifications);
            return getText(specifications);
        } catch (Exception e) {
            LOGGER.warning("Error getting product specifications: " + e.getMessage());
            return "";
        }
    }

    /**
     * Gets seller information
     *
     * @return Text of seller information
     */
    public String getSellerInfo() {
        try {
            LOGGER.info("Getting seller information");
            WebElement sellerInfo = isElementDisplayed(sellerInfoXpath) ?
                driver.findElement(sellerInfoXpath) :
                driver.findElement(sellerInfoCss);
                
            jsUtils.scrollToElement(sellerInfo);
            return getText(sellerInfo);
        } catch (Exception e) {
            LOGGER.warning("Error getting seller info: " + e.getMessage());
            return "";
        }
    }

    /**
     * Proceeds to checkout directly from product page
     *
     * @return CheckoutPage instance
     */
    public CheckoutPage buyNow() {
        try {
            LOGGER.info("Clicking Buy Now button");
            // Select first available color and size if present
            selectFirstColorOption();
            selectFirstSizeOption();
            
            // Click the Buy Now button
            WebElement buyNowButton = isElementDisplayed(buyNowButtonXpath) ?
                driver.findElement(buyNowButtonXpath) :
                driver.findElement(buyNowButtonCss);
                
            jsUtils.scrollToElement(buyNowButton);
            click(buyNowButton);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully clicked Buy Now button");
            return new CheckoutPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error clicking Buy Now button: " + e.getMessage());
            throw e;
        }
    }
}