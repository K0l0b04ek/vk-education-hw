package com.example.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.ui.elements.CartItem;
import com.example.ui.model.Product;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

/** Shopping cart page. */
public class CartPage extends LoadablePage<CartPage> {

    private static final String URL_PART = "/cart.html";

    private final SelenideElement title = $("[data-test='title']");
    private final SelenideElement continueShoppingButton = $("[data-test='continue-shopping']");

    public String title() {
        return title.text();
    }

    public List<Product> items() {
        return $$("[data-test='inventory-item']")
                .stream()
                .map(CartItem::new)
                .map(CartItem::toProduct)
                .toList();
    }

    public CartPage remove(Product product) {
        new CartItem($$("[data-test='inventory-item']").findBy(text(product.name()))).remove();
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

    /** Works when the user is already logged in; otherwise the site redirects to the login page. */
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
