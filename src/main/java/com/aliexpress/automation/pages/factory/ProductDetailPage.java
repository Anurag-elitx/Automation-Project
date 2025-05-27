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
 * Page Object for AliExpress Product Detail Page using Page Factory pattern
 */
public class ProductDetailPage {
    private static final Logger LOGGER = Logger.getLogger(ProductDetailPage.class.getName());
    private WebDriver driver;
    private WaitUtils waitUtils;
    private JavaScriptUtils jsUtils;

    @FindBy(css = "h1.product-title, .product-name")
    private WebElement productTitle;
    
    @FindBy(css = ".product-price .price, .price-current")
    private WebElement productPrice;
    
    @FindBy(css = "button.add-to-cart, .add-cart-btn")
    private WebElement addToCartButton;
    
    @FindBy(css = "button.buy-now, .buy-now-btn")
    private WebElement buyNowButton;
    
    @FindBy(css = ".product-gallery img, .gallery-image")
    private List<WebElement> productImages;
    
    @FindBy(css = ".sku-property.color .sku-property-item, .color-options .option-item")
    private List<WebElement> colorOptions;
    
    @FindBy(css = ".sku-property.size .sku-property-item, .size-options .option-item")
    private List<WebElement> sizeOptions;
    
    @FindBy(css = "input.quantity-input, input[type='number']")
    private WebElement quantityInput;
    
    @FindBy(css = ".quantity-increase, .increase-btn")
    private WebElement quantityIncreaseButton;
    
    @FindBy(css = ".quantity-decrease, .decrease-btn")
    private WebElement quantityDecreaseButton;
    
    @FindBy(css = ".product-description, #description")
    private WebElement productDescription;
    
    @FindBy(css = ".specifications, .product-specs")
    private WebElement productSpecifications;
    
    @FindBy(css = ".seller-info, .store-info")
    private WebElement sellerInfo;
    
    @FindBy(css = ".tab-reviews, .reviews-tab")
    private WebElement reviewsTab;
    
    @FindBy(css = ".tab-description, .description-tab")
    private WebElement descriptionTab;

    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        this.jsUtils = new JavaScriptUtils(driver);
        PageFactory.initElements(driver, this);
        LOGGER.info("Initialized Product Detail Page elements with Page Factory");
    }

    /**
     * Gets product title
     *
     * @return Product title text
     */
    public String getProductTitle() {
        try {
            return waitUtils.waitForElementToBeVisible(productTitle).getText();
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
            return waitUtils.waitForElementToBeVisible(productPrice).getText();
        } catch (Exception e) {
            LOGGER.severe("Error getting product price: " + e.getMessage());
            return "";
        }
    }

    /**
     * Selects first color option if available
     */
    public void selectFirstColorOption() {
        try {
            if (!colorOptions.isEmpty()) {
                LOGGER.info("Selecting first color option");
                WebElement firstColor = colorOptions.get(0);
                jsUtils.scrollToElement(firstColor);
                firstColor.click();
            } else {
                LOGGER.info("No color options available");
            }
        } catch (Exception e) {
            LOGGER.warning("Error selecting color option: " + e.getMessage());
        }
    }

    /**
     * Selects first size option if available
     */
    public void selectFirstSizeOption() {
        try {
            if (!sizeOptions.isEmpty()) {
                LOGGER.info("Selecting first size option");
                WebElement firstSize = sizeOptions.get(0);
                jsUtils.scrollToElement(firstSize);
                firstSize.click();
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
            
            // Try to set directly using input field
            try {
                waitUtils.waitForElementToBeVisible(quantityInput);
                quantityInput.clear();
                quantityInput.sendKeys(String.valueOf(quantity));
            } catch (Exception e) {
                LOGGER.info("Using +/- buttons to set quantity");
                
                // Reset to 1 using decrease button
                for (int i = 0; i < 10; i++) {
                    waitUtils.waitForElementToBeClickable(quantityDecreaseButton).click();
                    Thread.sleep(100);
                }
                
                // Increase to desired quantity
                for (int i = 1; i < quantity; i++) {
                    waitUtils.waitForElementToBeClickable(quantityIncreaseButton).click();
                    Thread.sleep(100);
                }
            }
            
            LOGGER.info("Successfully set quantity");
        } catch (Exception e) {
            LOGGER.severe("Error setting quantity: " + e.getMessage());
        }
    }

    /**
     * Adds product to cart
     *
     * @return CartPage instance
     */
    public CartPage addToCart() {
        try {
            LOGGER.info("Adding product to cart");
            
            // Select first color and size if available
            selectFirstColorOption();
            selectFirstSizeOption();
            
            // Click add to cart button
            jsUtils.scrollToElement(addToCartButton);
            waitUtils.waitForElementToBeClickable(addToCartButton).click();
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully added product to cart");
            return new CartPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error adding product to cart: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Proceeds directly to checkout
     *
     * @return CheckoutPage instance
     */
    public CheckoutPage buyNow() {
        try {
            LOGGER.info("Proceeding to buy now");
            
            // Select first color and size if available
            selectFirstColorOption();
            selectFirstSizeOption();
            
            // Click buy now button
            jsUtils.scrollToElement(buyNowButton);
            waitUtils.waitForElementToBeClickable(buyNowButton).click();
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully proceeded to checkout");
            return new CheckoutPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error proceeding to checkout: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Navigates to product description tab
     */
    public void navigateToDescription() {
        try {
            LOGGER.info("Navigating to description tab");
            jsUtils.scrollToElement(descriptionTab);
            descriptionTab.click();
            
            waitUtils.waitForElementToBeVisible(productDescription);
            jsUtils.scrollToElement(productDescription);
            LOGGER.info("Successfully navigated to description tab");
        } catch (Exception e) {
            LOGGER.warning("Error navigating to description tab: " + e.getMessage());
        }
    }

    /**
     * Navigates to product reviews tab
     */
    public void navigateToReviews() {
        try {
            LOGGER.info("Navigating to reviews tab");
            jsUtils.scrollToElement(reviewsTab);
            reviewsTab.click();
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully navigated to reviews tab");
        } catch (Exception e) {
            LOGGER.warning("Error navigating to reviews tab: " + e.getMessage());
        }
    }

    /**
     * Gets product description text
     *
     * @return Product description text
     */
    public String getProductDescription() {
        try {
            navigateToDescription();
            return productDescription.getText();
        } catch (Exception e) {
            LOGGER.severe("Error getting product description: " + e.getMessage());
            return "";
        }
    }

    /**
     * Gets product specifications text
     *
     * @return Product specifications text
     */
    public String getProductSpecifications() {
        try {
            jsUtils.scrollToElement(productSpecifications);
            return productSpecifications.getText();
        } catch (Exception e) {
            LOGGER.severe("Error getting product specifications: " + e.getMessage());
            return "";
        }
    }

    /**
     * Gets seller information
     *
     * @return Seller information text
     */
    public String getSellerInfo() {
        try {
            jsUtils.scrollToElement(sellerInfo);
            return sellerInfo.getText();
        } catch (Exception e) {
            LOGGER.severe("Error getting seller info: " + e.getMessage());
            return "";
        }
    }

    /**
     * Browses product images (clicks through gallery)
     */
    public void browseProductImages() {
        try {
            LOGGER.info("Browsing product images");
            
            for (WebElement image : productImages) {
                jsUtils.scrollToElement(image);
                image.click();
                Thread.sleep(500);
            }
            
            LOGGER.info("Finished browsing product images");
        } catch (Exception e) {
            LOGGER.warning("Error browsing product images: " + e.getMessage());
        }
    }
}