package com.example.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.math.BigDecimal;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

/** Product catalog page (opens after a successful login). */
public class InventoryPage {

    public enum SortOrder {
        NAME_A_TO_Z("az"),
        NAME_Z_TO_A("za"),
        PRICE_LOW_TO_HIGH("lohi"),
        PRICE_HIGH_TO_LOW("hilo");

        private final String optionValue;

        SortOrder(String optionValue) {
            this.optionValue = optionValue;
        }
    }

    private final SelenideElement title = $("[data-test='title']");
    private final SelenideElement sortSelect = $("[data-test='product-sort-container']");
    private final SelenideElement cartLink = $("[data-test='shopping-cart-link']");
    private final SelenideElement cartBadge = $("[data-test='shopping-cart-badge']");

    /** The constructor waits for the page, so having an instance means we really are on this page. */
    public InventoryPage() {
        webdriver().shouldHave(urlContaining("/inventory.html"));
        title.shouldBe(visible);
    }

    public String title() {
        return title.text();
    }

    public List<String> productNames() {
        return products().shouldHave(sizeGreaterThan(0)).texts();
    }

    public List<BigDecimal> productPrices() {
        return $$("[data-test='inventory-item-price']")
                .shouldHave(sizeGreaterThan(0))
                .texts()
                .stream()
                .map(price -> new BigDecimal(price.replace("$", "")))
                .toList();
    }

    public InventoryPage sortBy(SortOrder order) {
        sortSelect.selectOptionByValue(order.optionValue);
        return this;
    }

    public InventoryPage addToCart(String productName) {
        SelenideElement button = $$("[data-test='inventory-item']")
                .findBy(text(productName))
                .$("button");
        button.click();
        // wait until the UI switches the button, i.e. the product is really in the cart
        button.shouldHave(exactText("Remove"));
        return this;
    }

    /** Number shown on the cart badge. Expects at least one item to be in the cart. */
    public int cartCount() {
        return Integer.parseInt(cartBadge.shouldBe(visible).text());
    }

    public CartPage openCart() {
        cartLink.click();
        return new CartPage();
    }

    private ElementsCollection products() {
        return $$("[data-test='inventory-item-name']");
    }
}
