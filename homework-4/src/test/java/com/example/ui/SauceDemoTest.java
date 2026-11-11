package com.example.ui;

import com.example.ui.pages.CartPage;
import com.example.ui.pages.InventoryPage;
import com.example.ui.pages.InventoryPage.SortOrder;
import com.example.ui.pages.LoginPage;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SauceDemoTest extends BaseTest {

    private static final String STANDARD_USER = "standard_user";
    private static final String LOCKED_OUT_USER = "locked_out_user";
    private static final String PASSWORD = "secret_sauce";

    private static final Set<String> ALL_PRODUCTS = Set.of(
            "Sauce Labs Backpack",
            "Sauce Labs Bike Light",
            "Sauce Labs Bolt T-Shirt",
            "Sauce Labs Fleece Jacket",
            "Sauce Labs Onesie",
            "Test.allTheThings() T-Shirt (Red)");

    @Test
    void successfulLoginShowsFullProductCatalog() {
        InventoryPage inventory = new LoginPage().open().loginAs(STANDARD_USER, PASSWORD);
        List<String> names = inventory.productNames();
        assertEquals("Products", inventory.title());
        assertEquals(ALL_PRODUCTS.size(), names.size(), "unexpected number of products");
        assertEquals(ALL_PRODUCTS, new HashSet<>(names), "catalog contains unexpected products");
    }

    @Test
    void loginWithWrongPasswordShowsError() {
        LoginPage login = new LoginPage().open().loginExpectingError(STANDARD_USER, "wrong_password");
        assertEquals(
                "Epic sadface: Username and password do not match any user in this service",
                login.errorMessage());
    }

    @Test
    void lockedOutUserCannotLogin() {
        LoginPage login = new LoginPage().open().loginExpectingError(LOCKED_OUT_USER, PASSWORD);
        assertEquals("Epic sadface: Sorry, this user has been locked out.", login.errorMessage());
    }

    @Test
    void addedProductAppearsInCart() {
        String product = "Sauce Labs Backpack";
        InventoryPage inventory = new LoginPage().open().loginAs(STANDARD_USER, PASSWORD);
        inventory.addToCart(product);
        int badgeCount = inventory.cartCount();
        CartPage cart = inventory.openCart();
        assertEquals(1, badgeCount, "cart badge shows wrong number of items");
        assertEquals("Your Cart", cart.title());
        assertEquals(List.of(product), cart.itemNames(), "cart must contain exactly the added product");
    }

    @Test
    void sortingByPriceLowToHighOrdersPricesAndKeepsAllProducts() {
        InventoryPage inventory = new LoginPage().open().loginAs(STANDARD_USER, PASSWORD);
        List<String> namesBefore = inventory.productNames();
        List<BigDecimal> pricesBefore = inventory.productPrices();
        inventory.sortBy(SortOrder.PRICE_LOW_TO_HIGH);
        List<String> namesAfter = inventory.productNames();
        List<BigDecimal> pricesAfter = inventory.productPrices();
        List<BigDecimal> expectedPrices = pricesBefore.stream().sorted(Comparator.naturalOrder()).toList();
        assertEquals(expectedPrices, pricesAfter, "prices are not sorted ascending");
        assertEquals(namesBefore.size(), namesAfter.size(), "number of products changed after sorting");
        assertEquals(new HashSet<>(namesBefore), new HashSet<>(namesAfter), "set of products changed after sorting");
    }
}
