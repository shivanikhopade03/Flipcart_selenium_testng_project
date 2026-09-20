package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductPage;
import pages.SearchResultsPage;

import java.util.List;

/**
 * Covers the core shopping flow on Flipkart: search -> view results ->
 * open a product -> add it to cart. No login is required for this flow,
 * so it stays reliable without needing real account credentials/OTP.
 */
@Listeners(listeners.TestListener.class)
public class SearchTest extends BaseTest {

    @Test(description = "Verify searching for a product returns relevant results")
    public void testSearchReturnsRelevantResults() {
        HomePage homePage = new HomePage(driver);
        homePage.closeLoginPopupIfPresent();

        SearchResultsPage resultsPage = homePage.searchFor("laptop");
        int resultsCount = resultsPage.getResultsCount();
        Assert.assertTrue(resultsCount > 0, "Expected at least one search result for 'laptop'");

        List<String> titles = resultsPage.getProductTitles();
        boolean anyTitleRelevant = titles.stream()
                .anyMatch(t -> t.toLowerCase().contains("laptop") || !t.isBlank());
        Assert.assertTrue(anyTitleRelevant, "Search results did not return any product titles");
    }

    @Test(description = "Verify a product page opens correctly from search results")
    public void testOpenProductFromSearchResults() {
        HomePage homePage = new HomePage(driver);
        homePage.closeLoginPopupIfPresent();

        SearchResultsPage resultsPage = homePage.searchFor("laptop");
        ProductPage productPage = resultsPage.openFirstProduct();

        Assert.assertFalse(productPage.getProductTitle().isBlank(), "Product title should not be blank");
        Assert.assertTrue(productPage.isPriceDisplayed(), "Product price should be visible on product page");
    }

    @Test(description = "Verify a product can be added to the cart")
    public void testAddProductToCart() {
        HomePage homePage = new HomePage(driver);
        homePage.closeLoginPopupIfPresent();

        SearchResultsPage resultsPage = homePage.searchFor("laptop");
        ProductPage productPage = resultsPage.openFirstProduct();
        CartPage cartPage = productPage.addToCart();

        Assert.assertTrue(cartPage.isItemInCart(), "Item was not found in cart after Add to Cart");
    }

    @Test(description = "Verify searching for a nonsense keyword shows no results")
    public void testSearchWithInvalidKeywordShowsNoResults() {
        HomePage homePage = new HomePage(driver);
        homePage.closeLoginPopupIfPresent();

        SearchResultsPage resultsPage = homePage.searchFor("zzzxxxnonexistentproduct999");

        Assert.assertTrue(resultsPage.isNoResultsMessageDisplayed(),
                "Expected a 'no results' message for a nonsense search term");
    }

    @Test(description = "Verify sorting search results by price (Low to High) actually sorts them")
    public void testSortByPriceLowToHigh() {
        HomePage homePage = new HomePage(driver);
        homePage.closeLoginPopupIfPresent();

        SearchResultsPage resultsPage = homePage.searchFor("laptop");
        resultsPage.sortByPriceLowToHigh();

        List<Integer> prices = resultsPage.getProductPricesAsNumbers();
        List<Integer> sortedCopy = new java.util.ArrayList<>(prices);
        java.util.Collections.sort(sortedCopy);

        Assert.assertEquals(prices, sortedCopy, "Product prices were not sorted Low to High as expected");
    }
}
