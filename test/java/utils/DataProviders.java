package utils;

import org.testng.annotations.DataProvider;

/**
 * Central home for every @DataProvider used across test classes.
 * Keeping providers here (instead of inside each test class) means
 * the data source can be swapped (Excel/CSV/DB) without touching tests.
 */
public class DataProviders {

    private static final String SEARCH_DATA_PATH =
            "src/test/resources/testdata/SearchData.xlsx";

    /**
     * Supplies rows from SearchData.xlsx as {searchKeyword}.
     * TestNG calls the search test once per keyword, so adding a new
     * product to test means adding a new Excel row - no Java changes.
     */
    @DataProvider(name = "searchData")
    public static Object[][] searchData() {
        return ExcelUtils.getTestData(SEARCH_DATA_PATH, "SearchData");
    }
}
