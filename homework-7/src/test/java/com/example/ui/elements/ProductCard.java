package com.example.ui.elements;

import com.codeborne.selenide.SelenideElement;
import com.example.ui.locators.Locators;
import com.example.ui.model.Money;
import com.example.ui.model.Product;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;

public class ProductCard extends PageElement {

    private static final By NAME = Locators.testId("inventory-item-name");
    private static final By PRICE = Locators.testId("inventory-item-price");
    private static final By ADD_BUTTON = Locators.testIdStartsWith("add-to-cart");
    private static final By REMOVE_BUTTON = Locators.testIdStartsWith("remove");

    public ProductCard(SelenideElement root) {
        super(root);
    }

    public String name() {
        return child(NAME).text();
    }

    public Money price() {
        return Money.parse(child(PRICE).text());
    }

    public Product toProduct() {
        return new Product(name(), price());
    }

    public void addToCart() {
        child(ADD_BUTTON).click();
        child(REMOVE_BUTTON).shouldBe(visible);
    }

    public void removeFromCart() {
        child(REMOVE_BUTTON).click();
        child(ADD_BUTTON).shouldBe(visible);
    }
}
