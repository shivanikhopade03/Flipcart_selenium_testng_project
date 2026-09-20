package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductPage;
import pages.SearchResultsPage;

/**
 * Covers cart management beyond a single add-to-cart action: adding more
 * than one product across separate searches, and removing an item again.
 */
@Listeners(listeners.TestListener.class)
public class CartTest extends BaseTest {

    @Test(description = "Verify multiple different products can be added to the cart")
    public void testAddMultipleProductsToCart() {
        HomePage homePage = new HomePage(driver);
        homePage.closeLoginPopupIfPresent();

        // Add first product
        SearchResultsPage resultsPage = homePage.searchFor("laptop");
        ProductPage productPage = resultsPage.openFirstProduct();
        CartPage cartPage = productPage.addToCart();
        Assert.assertTrue(cartPage.isItemInCart(), "First product was not added to cart");

        // Go back to home and add a second, different product
        driver.get("https://www.flipkart.com/");
        homePage.closeLoginPopupIfPresent();
        SearchResultsPage secondResultsPage = homePage.searchFor("mobile phone");
        ProductPage secondProductPage = secondResultsPage.openFirstProduct();
        cartPage = secondProductPage.addToCart();

        int itemCount = cartPage.getCartItemCount();
        Assert.assertTrue(itemCount >= 2, "Expected at least 2 items in cart, found: " + itemCount);
    }

    @Test(description = "Verify a product can be removed from the cart")
    public void testRemoveProductFromCart() {
        HomePage homePage = new HomePage(driver);
        homePage.closeLoginPopupIfPresent();

        SearchResultsPage resultsPage = homePage.searchFor("laptop");
        ProductPage productPage = resultsPage.openFirstProduct();
        CartPage cartPage = productPage.addToCart();
        Assert.assertTrue(cartPage.isItemInCart(), "Product was not added to cart before removal");

        cartPage.removeFirstItem();

        Assert.assertTrue(cartPage.isCartEmpty(), "Cart should be empty after removing the only item");
    }
}
