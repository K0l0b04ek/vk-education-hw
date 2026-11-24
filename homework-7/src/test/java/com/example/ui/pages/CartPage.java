package com.example.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.ui.elements.CartItem;
import com.example.ui.locators.Locators;
import com.example.ui.model.Product;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class CartPage extends LoadablePage<CartPage> {

    private static final String URL_PART = "/cart.html";

    private final SelenideElement title = $(Locators.testId("title"));
    private final SelenideElement continueShoppingButton = $(Locators.testId("continue-shopping"));

    public String title() {
        return title.text();
    }

    public List<Product> items() {
        return $$(Locators.testId("inventory-item"))
                .stream()
                .map(CartItem::new)
                .map(CartItem::toProduct)
                .toList();
    }

    public CartPage remove(Product product) {
        new CartItem($(Locators.inventoryItemNamed(product.name()))).remove();
        return this;
    }

    public InventoryPage continueShopping() {
        continueShoppingButton.click();
        return new InventoryPage().await();
    }

    @Override
    protected boolean isLoaded() {
        return browserIsOn(URL_PART);
    }

    @Override
    protected void load() {
        Selenide.open(URL_PART);
    }

    @Override
    protected void verifyLoaded() {
        webdriver().shouldHave(urlContaining(URL_PART));
        title.shouldBe(visible);
    }

    @Override
    protected CartPage self() {
        return this;
    }
}
