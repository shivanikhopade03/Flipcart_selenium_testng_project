package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for Flipkart's search results / product listing page.
 * Reads back the product titles so tests can assert the search actually
 * returned relevant results, and can open the first product.
 */
public class SearchResultsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productTitles = By.cssSelector("div._4rR01T, a.s1Q9rs, a.IRpwTa");
    private final By firstProduct = By.cssSelector("div._4rR01T, a.s1Q9rs, a.IRpwTa");
    private final By productPrices = By.cssSelector("div._30jeq3");
    private final By sortLowToHigh = By.xpath("//div[text()='Price -- Low to High']");
    private final By noResultsMessage = By.xpath("//div[contains(text(),'did not match any products') or contains(text(),'Sorry')]");

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
    }

    public int getResultsCount() {
        wait.until(ExpectedConditions.presenceOfElementLocated(productTitles));
        return driver.findElements(productTitles).size();
    }

    public List<String> getProductTitles() {
        wait.until(ExpectedConditions.presenceOfElementLocated(productTitles));
        return driver.findElements(productTitles).stream()
                .map(WebElement::getText)
                .toList();
    }

    public ProductPage openFirstProduct() {
        wait.until(ExpectedConditions.presenceOfElementLocated(firstProduct));
        List<WebElement> products = driver.findElements(firstProduct);

        String originalWindow = driver.getWindowHandle();
        products.get(0).click();

        // Flipkart product links open in a new tab - switch driver focus to it.
        wait.until(d -> d.getWindowHandles().size() > 1);
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        return new ProductPage(driver);
    }

    /**
     * Clicks the "Price -- Low to High" sort option on the results page.
     */
    public void sortByPriceLowToHigh() {
        wait.until(ExpectedConditions.elementToBeClickable(sortLowToHigh)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(productPrices));
    }

    /**
     * Reads all visible product prices in listing order, parsed to plain
     * integers (commas and the currency symbol stripped), so a test can
     * assert the list is actually sorted ascending/descending.
     */
    public List<Integer> getProductPricesAsNumbers() {
        wait.until(ExpectedConditions.presenceOfElementLocated(productPrices));
        return driver.findElements(productPrices).stream()
                .map(WebElement::getText)
                .map(text -> text.replaceAll("[^0-9]", ""))
                .filter(text -> !text.isBlank())
                .map(Integer::parseInt)
                .toList();
    }

    /**
     * True when Flipkart shows its "no products found" message instead of
     * a results grid - used to assert a nonsense search returns nothing.
     */
    public boolean isNoResultsMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
