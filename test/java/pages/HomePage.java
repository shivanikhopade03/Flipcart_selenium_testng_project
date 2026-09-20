package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

/**
 * Page Object for the Flipkart home page. Flipkart shows a login popup
 * on first load which has to be dismissed before anything else on the
 * page can be interacted with reliably.
 */
public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By closeLoginPopup = By.cssSelector("button._2KpZ6l._2doB4z");
    private final By searchBox = By.name("q");
    private final By searchIcon = By.cssSelector("button[type='submit']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
    }

    /**
     * Closes the login popup if it appears. Not every session shows it
     * (depends on cookies/geo), so this never fails the test if it's absent.
     */
    public void closeLoginPopupIfPresent() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(closeLoginPopup)).click();
        } catch (Exception e) {
            // Popup did not appear this time - nothing to close, safe to continue.
        }
    }

    public SearchResultsPage searchFor(String product) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).sendKeys(product);
        driver.findElement(searchIcon).click();
        return new SearchResultsPage(driver);
    }
}
