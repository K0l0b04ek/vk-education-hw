package com.example.ui.cart;

import com.example.ui.BaseTest;
import com.example.ui.data.ProductRegistry;
import com.example.ui.model.Product;
import com.example.ui.pages.CartPage;
import com.example.ui.pages.InventoryPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("Adding each product to the cart")
@Tag("cart")
class AddEachProductToCartTest extends BaseTest {

    @TestFactory
    @Tag("regression")
    @Timeout(180)
    @DisplayName("every catalog product can be added to the cart")
    Stream<DynamicTest> everyProductCanBeAddedToCart() {
        return ProductRegistry.all().stream()
                .map(product -> dynamicTest(
                        "add '" + product.name() + "'",
                        () -> assertTimeout(Duration.ofSeconds(40), () -> verifyProductCanBeAdded(product))));
    }

    private void verifyProductCanBeAdded(Product product) {
        startFreshBrowser();
        InventoryPage inventory = openCatalogAsStandardUser();

        inventory.addToCart(product);
        int badgeCount = inventory.cartCount();
        CartPage cart = inventory.openCart();

        assertAll("adding " + product,
                () -> assertEquals(1, badgeCount, "wrong badge value"),
                () -> assertEquals(List.of(product), cart.items(), "cart must contain exactly this product"));
    }
}
