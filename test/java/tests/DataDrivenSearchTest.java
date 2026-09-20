package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchResultsPage;
import utils.DataProviders;

/**
 * Same search flow as SearchTest, but data-driven: search keywords come
 * from src/test/resources/testdata/SearchData.xlsx instead of being
 * hardcoded, so adding a new keyword to test means adding an Excel row,
 * not touching Java code.
 */
@Listeners(listeners.TestListener.class)
public class DataDrivenSearchTest extends BaseTest {

    @Test(dataProvider = "searchData", dataProviderClass = DataProviders.class,
          description = "Data-driven search using multiple keywords from Excel")
    public void testSearchWithMultipleKeywords(String keyword) {
        HomePage homePage = new HomePage(driver);
        homePage.closeLoginPopupIfPresent();

        SearchResultsPage resultsPage = homePage.searchFor(keyword);
        int resultsCount = resultsPage.getResultsCount();

        Assert.assertTrue(resultsCount > 0, "Expected at least one result for keyword: " + keyword);
    }
}
