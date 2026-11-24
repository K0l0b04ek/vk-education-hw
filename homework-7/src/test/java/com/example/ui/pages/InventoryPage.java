package com.example.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.ui.elements.CartLink;
import com.example.ui.elements.ProductCard;
import com.example.ui.elements.SortDropdown;
import com.example.ui.elements.SortOrder;
import com.example.ui.locators.Locators;
import com.example.ui.model.Product;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class InventoryPage extends LoadablePage<InventoryPage> {

    private static final String URL_PART = "/inventory.html";

    private final SelenideElement title = $(Locators.testId("title"));
    private final SortDropdown sortDropdown = new SortDropdown($(Locators.testId("product-sort-container")));
    private final CartLink cartLink = new CartLink($(Locators.testId("shopping-cart-link")));

    public String title() {
        return title.text();
    }

    public List<Product> products() {
        return $$(Locators.testId("inventory-item"))
                .shouldHave(sizeGreaterThan(0))
                .stream()
                .map(ProductCard::new)
                .map(ProductCard::toProduct)
                .toList();
    }

    public InventoryPage sortBy(SortOrder order) {
        sortDropdown.select(order);
        return this;
    }

    public InventoryPage addToCart(Product product) {
        card(product).addToCart();
        return this;
    }

    public InventoryPage removeFromCart(Product product) {
        card(product).removeFromCart();
        return this;
    }

    public int cartCount() {
        return cartLink.itemCount();
    }

    public CartPage openCart() {
        cartLink.open();
        return new CartPage().await();
    }

    private ProductCard card(Product product) {
        return new ProductCard($(Locators.inventoryItemNamed(product.name())));
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
    protected InventoryPage self() {
        return this;
    }
}
