package com.example.ui.locators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Locators")
@Tag("unit")
class LocatorsTest {

    @Test
    @DisplayName("testId builds an exact data-test CSS selector")
    void testIdCss() {
        assertEquals("[data-test='username']", Locators.testIdCss("username"));
    }

    @Test
    @DisplayName("testIdStartsWith builds a prefix data-test CSS selector")
    void testIdStartsWithCss() {
        assertEquals("[data-test^='add-to-cart']", Locators.testIdStartsWithCss("add-to-cart"));
    }

    @Test
    @DisplayName("inventory item XPath matches the exact product name")
    void inventoryItemXpath() {
        assertEquals(
                "//*[@data-test='inventory-item']"
                        + "[.//*[@data-test='inventory-item-name'][normalize-space()='Sauce Labs Backpack']]",
                Locators.inventoryItemNamedXpath("Sauce Labs Backpack"));
    }

    @Test
    @DisplayName("name with parentheses and dots is kept as a plain literal")
    void literalWithSpecialCharacters() {
        assertEquals("'Test.allTheThings() T-Shirt (Red)'",
                Locators.xpathLiteral("Test.allTheThings() T-Shirt (Red)"));
    }

    @Test
    @DisplayName("name with an apostrophe is wrapped in double quotes")
    void literalWithApostrophe() {
        assertEquals("\"Kid's Onesie\"", Locators.xpathLiteral("Kid's Onesie"));
    }

    @Test
    @DisplayName("name with both quote kinds is built with concat()")
    void literalWithBothQuotes() {
        assertEquals("concat('a', \"'\", 'b\"c')", Locators.xpathLiteral("a'b\"c"));
    }
}
