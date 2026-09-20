package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for the cart page shown right after "Add to cart".
 * Covers verifying items, reading item count, and removing an item.
 */
public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartItemTitle = By.cssSelector("div._2C41yL, div.DByuf8");
    private final By allCartItems = By.cssSelector("div._2C41yL, div.DByuf8");
    private final By placeOrderButton = By.xpath("//button[contains(text(),'PLACE ORDER')]");
    private final By removeItemLink = By.xpath("(//div[contains(text(),'REMOVE') or contains(text(),'Remove')])[1]");
    private final By emptyCartMessage = By.xpath("//div[contains(text(),'empty') or contains(text(),'Empty')]");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
    }

    public boolean isItemInCart() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(cartItemTitle)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlaceOrderButtonVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(placeOrderButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Number of distinct product line items currently shown in the cart.
     */
    public int getCartItemCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(allCartItems));
            List<org.openqa.selenium.WebElement> items = driver.findElements(allCartItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Removes the first item shown in the cart.
     */
    public void removeFirstItem() {
        wait.until(ExpectedConditions.elementToBeClickable(removeItemLink)).click();
    }

    public boolean isCartEmpty() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
