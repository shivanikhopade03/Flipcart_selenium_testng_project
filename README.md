# Selenium + Java + TestNG Automation Framework

An end-to-end UI automation framework built for testing **Flipkart**
(https://www.flipkart.com) — covers the core shopping flow: search a product,
view its details, and add it to cart. This flow deliberately avoids login,
since real e-commerce login requires OTP/captcha that can't be automated
reliably — this is a standard, defensible choice to explain in interviews.

## Tech Stack
- **Java 11**
- **Selenium WebDriver 4**
- **TestNG** — test runner, assertions, suite management
- **Maven** — build & dependency management
- **WebDriverManager** — automatic browser driver binaries (no manual chromedriver downloads)
- **ExtentReports** — rich HTML test reports with pass/fail/skip status
- **Log4j2** — structured logging to console and file
- **Apache POI** — reads Excel test data for data-driven testing
- **GitHub Actions** — CI pipeline that runs the suite headless on every push

## Design
- **Page Object Model (POM)**: each page (`HomePage`, `SearchResultsPage`,
  `ProductPage`, `CartPage`) encapsulates its own locators and actions. Test
  classes never touch a `By` or raw `WebElement`.
- **BaseTest**: centralizes WebDriver setup/teardown via TestNG's
  `@BeforeMethod`/`@AfterMethod`, with stability flags and a one-time retry
  around Chrome session creation to absorb transient launch failures.
- **ConfigReader**: externalizes environment values (URL, browser, timeouts)
  into `config.properties` — no hardcoded strings in test logic.
- **TestListener**: implements `ITestListener` to auto-log every test's
  result into the Extent report, no manual logging calls needed inside
  `@Test` methods.

## Project Structure
```
selenium-framework/
├── src/test/java/
│   ├── base/BaseTest.java
│   ├── pages/            (HomePage, SearchResultsPage, ProductPage, CartPage)
│   ├── tests/             (SearchTest, CartTest, DataDrivenSearchTest)
│   ├── utils/             (ConfigReader, ExtentReportManager, ExcelUtils, DataProviders)
│   └── listeners/TestListener.java
├── src/test/resources/
│   ├── config.properties
│   ├── log4j2.xml
│   └── testdata/SearchData.xlsx
├── testng.xml
├── pom.xml
└── .github/workflows/maven.yml
```

## How to Run
```bash
mvn clean test
```
Reports are generated under `/reports/TestReport_<timestamp>.html` after each run.
Logs are written to `/logs/automation.log`.

## CI/CD
Every push to `main` triggers the GitHub Actions workflow (`.github/workflows/maven.yml`),
which runs the suite headless on Ubuntu and uploads the Extent report + Surefire results
as build artifacts.

## Test Coverage
| Test Class            | Scenario                                                       |
|------------------------|------------------------------------------------------------------|
| SearchTest             | Search returns results, opens a product, adds a product to cart, invalid search shows no results, sorting by price Low-to-High |
| CartTest               | Adding multiple different products to cart, removing a product from cart |
| DataDrivenSearchTest   | Same search flow repeated for multiple keywords, sourced from `testdata/SearchData.xlsx` |

## Data-Driven Testing
`DataDrivenSearchTest` doesn't hardcode a single keyword — it reads rows from
`src/test/resources/testdata/SearchData.xlsx` through `ExcelUtils` (Apache POI)
and TestNG's `@DataProvider` (defined centrally in `utils.DataProviders`).
Adding a new keyword to test means adding a new Excel row — no Java code
changes needed. TestNG runs the test method once per row automatically.

## Note on Real Websites
Real production sites like Flipkart change their DOM/CSS class names over
time and may show anti-bot checks occasionally. Locators here were verified
at the time of writing; if a locator breaks, that's expected maintenance —
being able to diagnose and update a broken locator is itself a strong
interview talking point about real-world automation.

## Possible Extensions (good interview talking points)
- Cross-browser execution (Firefox/Edge) driven by `config.properties`
- Parallel execution via `testng.xml` `parallel="methods"`
- Dockerized Selenium Grid for CI
- Screenshot-on-failure attached directly into the Extent report
- Checkout flow simulation (address, payment page navigation without submitting a real order)
