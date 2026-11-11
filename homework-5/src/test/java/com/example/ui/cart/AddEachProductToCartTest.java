package com.example.ui.cart;

import com.example.ui.BaseTest;
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

import static com.example.ui.TestData.ALL_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * One dynamic test per product.
 *
 * <p>Note: JUnit runs @BeforeEach/@AfterEach once for the whole @TestFactory, not for every
 * dynamic test. That is why this class has no @BeforeEach login and every dynamic test starts
 * its own fresh browser: otherwise the cart of one product would leak into the next one.
 */
@DisplayName("Adding each product to the cart")
@Tag("cart")
class AddEachProductToCartTest extends BaseTest {

    @TestFactory
    @Tag("regression")
    @Timeout(180)
    @DisplayName("every catalog product can be added to the cart")
    Stream<DynamicTest> everyProductCanBeAddedToCart() {
        return ALL_PRODUCTS.stream()
                .map(product -> dynamicTest(
                        "add '" + product + "'",
                        // assertTimeout runs in the same thread (Selenide's driver is thread-bound);
                        // @Timeout on the factory only covers creating the stream, not the dynamic tests
                        () -> assertTimeout(Duration.ofSeconds(40), () -> verifyProductCanBeAdded(product))));
    }

    private void verifyProductCanBeAdded(String product) {
        startFreshBrowser();
        InventoryPage inventory = openCatalogAsStandardUser();

        inventory.addToCart(product);
        int badgeCount = inventory.cartCount();
        CartPage cart = inventory.openCart();

        assertAll("adding '" + product + "'",
                () -> assertEquals(1, badgeCount, "wrong badge value"),
                () -> assertEquals(List.of(product), cart.itemNames(), "cart must contain exactly this product"));
    }
}
