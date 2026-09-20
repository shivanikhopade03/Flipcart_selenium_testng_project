package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

/**
 * Page Object for a Flipkart product detail page - title, price, and the
 * add-to-cart action that a real shopping flow depends on.
 */
public class ProductPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productTitle = By.cssSelector("span.B_NuCI, span.VU-ZEz");
    private final By productPrice = By.cssSelector("div._30jeq3, div.Nx9bqj");
    private final By addToCartButton = By.xpath("//button[contains(text(),'ADD TO CART')]");
    private final By cartIcon = By.cssSelector("a[href='/viewcart']");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
    }

    public String getProductTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle)).getText();
    }

    public boolean isPriceDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productPrice)).isDisplayed();
    }

    public CartPage addToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        return new CartPage(driver);
    }
}
