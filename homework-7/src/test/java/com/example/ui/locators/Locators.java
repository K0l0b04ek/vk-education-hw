package com.example.ui.locators;

import org.openqa.selenium.By;

import java.util.Arrays;

public final class Locators {

    private static final String TEST_ATTRIBUTE = "data-test";

    private Locators() {}

    public static By testId(String id) {
        return By.cssSelector(testIdCss(id));
    }

    public static By testIdStartsWith(String prefix) {
        return By.cssSelector(testIdStartsWithCss(prefix));
    }

    public static String testIdCss(String id) {
        return "[" + TEST_ATTRIBUTE + "='" + id + "']";
    }

    public static String testIdStartsWithCss(String prefix) {
        return "[" + TEST_ATTRIBUTE + "^='" + prefix + "']";
    }

    public static By inventoryItemNamed(String productName) {
        return By.xpath(inventoryItemNamedXpath(productName));
    }

    public static String inventoryItemNamedXpath(String productName) {
        return "//*[@" + TEST_ATTRIBUTE + "='inventory-item']"
                + "[.//*[@" + TEST_ATTRIBUTE + "='inventory-item-name']"
                + "[normalize-space()=" + xpathLiteral(productName) + "]]";
    }

    public static String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        String[] parts = value.split("'", -1);
        return "concat(" + String.join(", \"'\", ",
                Arrays.stream(parts).map(part -> "'" + part + "'").toList()) + ")";
    }
}
