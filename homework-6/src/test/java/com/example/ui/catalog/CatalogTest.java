package com.example.ui.catalog;

import com.example.ui.BaseTest;
import com.example.ui.data.ProductRegistry;
import com.example.ui.data.ProductsProvider;
import com.example.ui.data.UserFactory;
import com.example.ui.elements.SortOrder;
import com.example.ui.model.Money;
import com.example.ui.model.Product;
import com.example.ui.pages.InventoryPage;
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
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
    @DisplayName("shows exactly the expected set of products")
    void showsExpectedProducts() {
        List<String> names = inventory.products().stream().map(Product::name).toList();
        Set<String> expectedNames = ProductRegistry.all().stream().map(Product::name).collect(Collectors.toSet());

        assertAll("catalog content",
                () -> assertEquals(expectedNames.size(), names.size(), "unexpected number of products"),
                () -> assertEquals(expectedNames, Set.copyOf(names), "unexpected set of products"));
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ProductsProvider.class)
    @Tag("regression")
    @DisplayName("every product is shown with its registered price")
    void productHasRegisteredPrice(Product expected) {
        Product actual = inventory.products().stream()
                .filter(product -> product.name().equals(expected.name()))
                .findFirst()
                .orElseGet(() -> fail("product is missing in the catalog: " + expected.name()));

        assertEquals(expected, actual);
    }

    @Nested
    @DisplayName("sorting")
    @Tag("regression")
    class Sorting {

        @ParameterizedTest(name = "{0} orders products by name")
        @MethodSource("com.example.ui.catalog.CatalogTest#nameSortCases")
        @DisplayName("by name")
        void sortsByName(SortOrder order, Comparator<String> expectedOrder) {
            List<Product> before = inventory.products();

            List<Product> after = inventory.sortBy(order).products();

            assertEquals(
                    before.stream().map(Product::name).sorted(expectedOrder).toList(),
                    after.stream().map(Product::name).toList(),
                    "products are not sorted by name: " + order);
        }

        @ParameterizedTest(name = "{0} orders products by price")
        @MethodSource("com.example.ui.catalog.CatalogTest#priceSortCases")
        @DisplayName("by price")
        void sortsByPrice(SortOrder order, Comparator<Money> expectedOrder) {
            List<Product> before = inventory.products();

            List<Product> after = inventory.sortBy(order).products();

            assertAll("sorting by price: " + order,
                    () -> assertEquals(
                            before.stream().map(Product::price).sorted(expectedOrder).toList(),
                            after.stream().map(Product::price).toList(),
                            "prices are not sorted"),
                    () -> assertEquals(byName(before), byName(after),
                            "sorting lost, duplicated or replaced products"));
        }

        @Test
        @Disabled("Known SauceDemo behavior: sorting is intentionally broken for problem_user. "
                + "Enable when testing against a build where it is fixed.")
        @DisplayName("works for problem_user")
        void problemUserCanSortByPrice() {
            InventoryPage page = new LoginPage().get().loginAs(UserFactory.problem());
            List<Money> before = page.products().stream().map(Product::price).toList();

            List<Money> after = page.sortBy(SortOrder.PRICE_LOW_TO_HIGH).products().stream()
                    .map(Product::price).toList();

            assertEquals(before.stream().sorted().toList(), after);
        }
    }

    static Stream<Arguments> nameSortCases() {
        return Stream.of(
                Arguments.of(SortOrder.NAME_A_TO_Z, Comparator.<String>naturalOrder()),
                Arguments.of(SortOrder.NAME_Z_TO_A, Comparator.<String>reverseOrder()));
    }

    static Stream<Arguments> priceSortCases() {
        return Stream.of(
                Arguments.of(SortOrder.PRICE_LOW_TO_HIGH, Comparator.<Money>naturalOrder()),
                Arguments.of(SortOrder.PRICE_HIGH_TO_LOW, Comparator.<Money>reverseOrder()));
    }

    private static List<Product> byName(List<Product> products) {
        return products.stream().sorted(Comparator.comparing(Product::name)).toList();
    }
}
