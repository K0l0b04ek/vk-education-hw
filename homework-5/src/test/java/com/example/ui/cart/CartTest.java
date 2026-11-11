package com.example.ui.cart;

import com.example.ui.BaseTest;
import com.example.ui.pages.CartPage;
import com.example.ui.pages.InventoryPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Shopping cart")
@Tag("cart")
@Timeout(60)
class CartTest extends BaseTest {

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";
    private static final String ONESIE = "Sauce Labs Onesie";

    private InventoryPage inventory;

    /** Runs before the @BeforeEach methods of the nested classes below. */
    @BeforeEach
    void openCatalog() {
        inventory = openCatalogAsStandardUser();
    }

    @Nested
    @DisplayName("when nothing was added")
    class EmptyCart {

        @Test
        @Tag("smoke")
        @DisplayName("has no badge and no items")
        void cartIsEmpty() {
            int badgeCount = inventory.cartCount();
            CartPage cart = inventory.openCart();

            assertAll("empty cart",
                    () -> assertEquals(0, badgeCount, "badge must not show items"),
                    () -> assertEquals("Your Cart", cart.title()),
                    () -> assertEquals(List.of(), cart.itemNames(), "cart must be empty"));
        }
    }

    @Nested
    @DisplayName("after adding one product")
    class OneProduct {

        @BeforeEach
        void addBackpack() {
            inventory.addToCart(BACKPACK);
        }

        @Test
        @Tag("smoke")
        @DisplayName("badge shows 1 and cart contains exactly that product")
        void badgeAndCartContentMatch() {
            int badgeCount = inventory.cartCount();
            CartPage cart = inventory.openCart();

            assertAll("cart with one product",
                    () -> assertEquals(1, badgeCount, "wrong badge value"),
                    () -> assertEquals(List.of(BACKPACK), cart.itemNames(), "wrong cart content"));
        }

        @Test
        @Tag("regression")
        @DisplayName("removing it on the catalog page clears the badge and the cart")
        void removalOnCatalogPageEmptiesCart() {
            inventory.removeFromCart(BACKPACK);
            int badgeCount = inventory.cartCount();
            CartPage cart = inventory.openCart();

            assertAll("cart after removal on catalog page",
                    () -> assertEquals(0, badgeCount, "badge must disappear"),
                    () -> assertEquals(List.of(), cart.itemNames(), "cart must be empty"));
        }

        @Test
        @Tag("regression")
        @DisplayName("removing it on the cart page empties the cart")
        void removalOnCartPageEmptiesCart() {
            CartPage cart = inventory.openCart();

            cart.remove(BACKPACK);

            assertEquals(List.of(), cart.itemNames(), "cart must be empty");
        }
    }

    @Nested
    @DisplayName("after adding three products")
    class SeveralProducts {

        private final List<String> added = List.of(BACKPACK, BIKE_LIGHT, ONESIE);

        @BeforeEach
        void addProducts() {
            added.forEach(inventory::addToCart);
        }

        @Test
        @Tag("regression")
        @DisplayName("badge shows 3 and cart contains exactly the added products")
        void cartContainsExactlyAddedProducts() {
            int badgeCount = inventory.cartCount();
            List<String> names = inventory.openCart().itemNames();

            assertAll("cart with three products",
                    () -> assertEquals(added.size(), badgeCount, "wrong badge value"),
                    () -> assertEquals(added.size(), names.size(), "wrong number of items in cart"),
                    () -> assertEquals(Set.copyOf(added), Set.copyOf(names), "wrong set of items in cart"));
        }
    }
}
