package com.aliexpress.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.logging.Logger;

/**
 * Page Object for AliExpress Shopping Cart Page
 */
public class CartPage extends BasePage {
    private static final Logger LOGGER = Logger.getLogger(CartPage.class.getName());

    // XPath locators
    private final By cartItemsXpath = By.xpath("//div[contains(@class, 'cart-item') or contains(@class, 'item-content')]");
    private final By checkoutButtonXpath = By.xpath("//button[contains(@class, 'checkout') or contains(text(), 'Proceed to Checkout')]");
    private final By removeItemButtonXpath = By.xpath("//button[contains(@class, 'remove') or contains(@class, 'delete')]");
    private final By itemPriceXpath = By.xpath("//span[contains(@class, 'price') or contains(@class, 'item-price')]");
    private final By totalAmountXpath = By.xpath("//div[contains(@class, 'total-price') or contains(@class, 'grand-total')]//span[contains(@class, 'price')]");
    private final By quantityInputXpath = By.xpath("//input[contains(@class, 'quantity') or @type='number']");
    private final By emptyCartMessageXpath = By.xpath("//div[contains(@class, 'empty-cart') or contains(text(), 'Your shopping cart is empty')]");
    private final By itemTitleXpath = By.xpath("//span[contains(@class, 'item-title') or contains(@class, 'product-name')]");

    // CSS selectors
    private final By cartItemsCss = By.cssSelector(".cart-item, .item-content");
    private final By checkoutButtonCss = By.cssSelector("button.checkout, .proceed-checkout");
    private final By removeItemButtonCss = By.cssSelector("button.remove-item, .delete-btn");
    private final By itemPriceCss = By.cssSelector(".item-price, .price");
    private final By totalAmountCss = By.cssSelector(".total-price .price, .grand-total .price-value");
    private final By quantityInputCss = By.cssSelector("input.quantity-input, input[type='number']");
    private final By emptyCartMessageCss = By.cssSelector(".empty-cart, .cart-empty-message");
    private final By itemTitleCss = By.cssSelector(".item-title, .product-name");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Gets all cart items
     *
     * @return List of WebElements representing cart items
     */
    public List<WebElement> getCartItems() {
        try {
            LOGGER.info("Getting all cart items");
            waitUtils.waitForPageLoad();
            return driver.findElements(cartItemsCss);
        } catch (Exception e) {
            LOGGER.severe("Error getting cart items: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Gets count of items in cart
     *
     * @return Count of items in cart
     */
    public int getCartItemCount() {
        try {
            return getCartItems().size();
        } catch (Exception e) {
            LOGGER.severe("Error getting cart item count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Checks if cart is empty
     *
     * @return true if cart is empty, false otherwise
     */
    public boolean isCartEmpty() {
        try {
            LOGGER.info("Checking if cart is empty");
            return isElementDisplayed(emptyCartMessageXpath) || 
                   isElementDisplayed(emptyCartMessageCss) ||
                   getCartItemCount() == 0;
        } catch (Exception e) {
            LOGGER.warning("Error checking if cart is empty: " + e.getMessage());
            return true;
        }
    }

    /**
     * Removes an item from cart by index
     *
     * @param index Index of item to remove (0-based)
     */
    public void removeItemByIndex(int index) {
        try {
            LOGGER.info("Removing item at index: " + index);
            List<WebElement> cartItems = getCartItems();
            
            if (index < 0 || index >= cartItems.size()) {
                throw new IndexOutOfBoundsException("Cart item index out of bounds: " + index);
            }
            
            WebElement item = cartItems.get(index);
            WebElement removeButton = item.findElement(removeItemButtonCss);
            
            jsUtils.scrollToElement(removeButton);
            click(removeButton);
            
            // Handle confirmation dialog if present
            try {
                WebElement confirmButton = waitUtils.waitForElementToBeClickable(By.xpath("//button[contains(text(), 'OK') or contains(text(), 'Yes')]"));
                click(confirmButton);
            } catch (Exception e) {
                LOGGER.info("No confirmation dialog found or it was automatically dismissed");
            }
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully removed item from cart");
        } catch (Exception e) {
            LOGGER.severe("Error removing item from cart: " + e.getMessage());
        }
    }

    /**
     * Gets total price amount
     *
     * @return Total price as string
     */
    public String getTotalAmount() {
        try {
            LOGGER.info("Getting cart total amount");
            WebElement totalElement = isElementDisplayed(totalAmountXpath) ?
                driver.findElement(totalAmountXpath) :
                driver.findElement(totalAmountCss);
                
            return getText(totalElement);
        } catch (Exception e) {
            LOGGER.severe("Error getting total amount: " + e.getMessage());
            return "";
        }
    }

    /**
     * Updates quantity of an item by index
     *
     * @param index    Index of item (0-based)
     * @param quantity New quantity to set
     */
    public void updateItemQuantity(int index, int quantity) {
        try {
            LOGGER.info("Updating quantity of item at index " + index + " to " + quantity);
            List<WebElement> cartItems = getCartItems();
            
            if (index < 0 || index >= cartItems.size()) {
                throw new IndexOutOfBoundsException("Cart item index out of bounds: " + index);
            }
            
            WebElement item = cartItems.get(index);
            WebElement quantityInput = item.findElement(quantityInputCss);
            
            jsUtils.scrollToElement(quantityInput);
            
            // Try to set directly, if not possible use JavaScript
            try {
                type(quantityInput, String.valueOf(quantity));
                quantityInput.sendKeys(Keys.TAB); // Trigger onblur event
            } catch (Exception e) {
                LOGGER.info("Using JavaScript to set quantity");
                jsUtils.setValueToElement(quantityInput, String.valueOf(quantity));
                jsUtils.executeScript("arguments[0].dispatchEvent(new Event('change'))", quantityInput);
            }
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully updated item quantity");
        } catch (Exception e) {
            LOGGER.severe("Error updating item quantity: " + e.getMessage());
        }
    }

    /**
     * Proceeds to checkout
     *
     * @return CheckoutPage instance
     */
    public CheckoutPage proceedToCheckout() {
        try {
            LOGGER.info("Proceeding to checkout");
            WebElement checkoutButton = isElementDisplayed(checkoutButtonXpath) ?
                driver.findElement(checkoutButtonXpath) :
                driver.findElement(checkoutButtonCss);
                
            jsUtils.scrollToElement(checkoutButton);
            click(checkoutButton);
            
            waitUtils.waitForPageLoad();
            LOGGER.info("Successfully proceeded to checkout");
            return new CheckoutPage(driver);
        } catch (Exception e) {
            LOGGER.severe("Error proceeding to checkout: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Gets item title by index
     *
     * @param index Index of item (0-based)
     * @return Title of the item
     */
    public String getItemTitle(int index) {
        try {
            LOGGER.info("Getting title of item at index: " + index);
            List<WebElement> cartItems = getCartItems();
            
            if (index < 0 || index >= cartItems.size()) {
                throw new IndexOutOfBoundsException("Cart item index out of bounds: " + index);
            }
            
            WebElement item = cartItems.get(index);
            WebElement titleElement = item.findElement(itemTitleCss);
            
            return getText(titleElement);
        } catch (Exception e) {
            LOGGER.severe("Error getting item title: " + e.getMessage());
            return "";
        }
    }

    /**
     * Gets item price by index
     *
     * @param index Index of item (0-based)
     * @return Price of the item
     */
    public String getItemPrice(int index) {
        try {
            LOGGER.info("Getting price of item at index: " + index);
            List<WebElement> cartItems = getCartItems();
            
            if (index < 0 || index >= cartItems.size()) {
                throw new IndexOutOfBoundsException("Cart item index out of bounds: " + index);
            }
            
            WebElement item = cartItems.get(index);
            WebElement priceElement = item.findElement(itemPriceCss);
            
            return getText(priceElement);
        } catch (Exception e) {
            LOGGER.severe("Error getting item price: " + e.getMessage());
            return "";
        }
    }
}