package com.example.ui.catalog;

import com.example.ui.BaseTest;
import com.example.ui.pages.InventoryPage;
import com.example.ui.pages.InventoryPage.SortOrder;
import com.example.ui.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.example.ui.TestData.ALL_PRICES_ASCENDING;
import static com.example.ui.TestData.ALL_PRODUCTS;
import static com.example.ui.TestData.PASSWORD;
import static com.example.ui.TestData.PROBLEM_USER;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Product catalog")
@Tag("catalog")
@Timeout(60)
class CatalogTest extends BaseTest {

    private InventoryPage inventory;

    @BeforeEach
    void openCatalog() {
        inventory = openCatalogAsStandardUser();
    }

    @Test
    @Tag("smoke")
    @Timeout(30)
    @DisplayName("shows exactly the expected products and prices")
    void showsExpectedProductsAndPrices() {
        List<String> names = inventory.productNames();
        List<BigDecimal> prices = inventory.productPrices();

        assertAll("catalog content",
                () -> assertEquals(ALL_PRODUCTS.size(), names.size(), "unexpected number of products"),
                () -> assertEquals(Set.copyOf(ALL_PRODUCTS), Set.copyOf(names), "unexpected set of products"),
                () -> assertEquals(ALL_PRICES_ASCENDING, prices.stream().sorted().toList(), "unexpected prices"));
    }

    @Nested
    @DisplayName("sorting")
    @Tag("regression")
    class Sorting {

        @ParameterizedTest(name = "{0} orders products by name")
        @MethodSource("com.example.ui.catalog.CatalogTest#nameSortCases")
        @DisplayName("by name")
        void sortsByName(SortOrder order, Comparator<String> expectedOrder) {
            List<String> before = inventory.productNames();

            inventory.sortBy(order);
            List<String> after = inventory.productNames();

            // comparing with a sorted copy of the original list also proves that
            // no product was lost, duplicated or replaced by sorting
            assertEquals(before.stream().sorted(expectedOrder).toList(), after,
                    "products are not sorted by name: " + order);
        }

        @ParameterizedTest(name = "{0} orders products by price")
        @MethodSource("com.example.ui.catalog.CatalogTest#priceSortCases")
        @DisplayName("by price")
        void sortsByPrice(SortOrder order, Comparator<BigDecimal> expectedOrder) {
            List<String> namesBefore = inventory.productNames();
            List<BigDecimal> pricesBefore = inventory.productPrices();

            inventory.sortBy(order);
            List<String> namesAfter = inventory.productNames();
            List<BigDecimal> pricesAfter = inventory.productPrices();

            assertAll("sorting by price: " + order,
                    () -> assertEquals(pricesBefore.stream().sorted(expectedOrder).toList(), pricesAfter,
                            "prices are not sorted"),
                    () -> assertEquals(sorted(namesBefore), sorted(namesAfter),
                            "sorting lost, duplicated or replaced products"));
        }

        @Test
        @Disabled("Known SauceDemo behavior: sorting is intentionally broken for problem_user. "
                + "Enable when testing against a build where it is fixed.")
        @DisplayName("works for problem_user")
        void problemUserCanSortByPrice() {
            InventoryPage page = new LoginPage().open().loginAs(PROBLEM_USER, PASSWORD);
            List<BigDecimal> before = page.productPrices();

            page.sortBy(SortOrder.PRICE_LOW_TO_HIGH);

            assertEquals(before.stream().sorted().toList(), page.productPrices());
        }
    }

    static Stream<Arguments> nameSortCases() {
        return Stream.of(
                Arguments.of(SortOrder.NAME_A_TO_Z, Comparator.<String>naturalOrder()),
                Arguments.of(SortOrder.NAME_Z_TO_A, Comparator.<String>reverseOrder()));
    }

    static Stream<Arguments> priceSortCases() {
        return Stream.of(
                Arguments.of(SortOrder.PRICE_LOW_TO_HIGH, Comparator.<BigDecimal>naturalOrder()),
                Arguments.of(SortOrder.PRICE_HIGH_TO_LOW, Comparator.<BigDecimal>reverseOrder()));
    }

    private static List<String> sorted(List<String> names) {
        return names.stream().sorted().toList();
    }
}
