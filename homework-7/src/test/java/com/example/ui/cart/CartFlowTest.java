package com.example.ui.cart;

import com.example.ui.BaseTest;
import com.example.ui.data.UserFactory;
import com.example.ui.model.Product;
import com.example.ui.pages.CartPage;
import com.example.ui.pages.InventoryPage;
import com.example.ui.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.Set;

import static com.example.ui.data.ProductRegistry.BACKPACK;
import static com.example.ui.data.ProductRegistry.BIKE_LIGHT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Cart flows across pages")
@Tag("cart")
@Timeout(60)
class CartFlowTest extends BaseTest {

    @Test
    @Tag("regression")
    @DisplayName("chain: login -> add -> cart -> continue shopping -> add -> cart")
    void userBuildsCartAcrossPages() {
        InventoryPage inventory = new LoginPage().get()
                .loginAs(UserFactory.standard())
                .addToCart(BACKPACK)
                .openCart()
                .continueShopping()
                .addToCart(BIKE_LIGHT);

        int badgeCount = inventory.cartCount();
        List<Product> items = inventory.openCart().items();

        assertAll("cart built across pages",
                () -> assertEquals(2, badgeCount, "wrong badge value"),
                () -> assertEquals(2, items.size(), "wrong number of items in cart"),
                () -> assertEquals(Set.of(BACKPACK, BIKE_LIGHT), Set.copyOf(items), "wrong set of items in cart"));
    }

    @Test
    @Tag("regression")
    @DisplayName("loadable page: the cart can be opened directly by URL and keeps its content")
    void cartCanBeOpenedDirectly() {
        new LoginPage().get()
                .loginAs(UserFactory.standard())
                .addToCart(BACKPACK);

        CartPage cart = new CartPage().get();

        assertAll("cart opened by URL",
                () -> assertEquals("Your Cart", cart.title()),
                () -> assertEquals(List.of(BACKPACK), cart.items(), "cart content was lost"));
    }
}
