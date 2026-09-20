package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;

import java.time.Duration;

/**
 * Every test class extends this. It owns the WebDriver lifecycle
 * (create before each test, quit after) so individual test classes
 * never touch driver setup directly.
 */
public class BaseTest {

    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.get("browser");
        logger.info("Launching browser: " + browser);

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if (ConfigReader.getBoolean("headless")) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            // Stability flags: reduce "session not created / Chrome instance exited"
            // failures caused by leftover Chrome processes or sandbox conflicts
            // when tests launch a new Chrome instance back-to-back.
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--remote-allow-origins=*");

            driver = createChromeDriverWithRetry(options);
        } else {
            throw new RuntimeException("Browser not supported yet: " + browser);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait")));
        driver.get(ConfigReader.get("url"));
        logger.info("Navigated to: " + ConfigReader.get("url"));
    }

    /**
     * Chrome occasionally fails its very first launch attempt with
     * SessionNotCreatedException if a previous instance hasn't fully
     * released its resources yet. One short-delay retry clears this up
     * without masking a genuine, persistent driver/browser mismatch.
     */
    private WebDriver createChromeDriverWithRetry(ChromeOptions options) {
        try {
            return new ChromeDriver(options);
        } catch (org.openqa.selenium.SessionNotCreatedException e) {
            logger.warn("Chrome session failed to start, retrying once: " + e.getMessage());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return new ChromeDriver(options);
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            logger.info("Closing browser");
            driver.quit();
        }
    }
}
