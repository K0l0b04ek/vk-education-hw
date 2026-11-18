package com.example.ui.elements;

import com.codeborne.selenide.SelenideElement;
import com.example.ui.model.Money;
import com.example.ui.model.Product;

import static com.codeborne.selenide.Condition.exactText;

public class ProductCard extends PageElement {

    public ProductCard(SelenideElement root) {
        super(root);
    }

    public String name() {
        return child("[data-test='inventory-item-name']").text();
    }

    public Money price() {
        return Money.parse(child("[data-test='inventory-item-price']").text());
    }

    public Product toProduct() {
        return new Product(name(), price());
    }

    public void addToCart() {
        SelenideElement button = child("button");
        button.click();
        button.shouldHave(exactText("Remove"));
    }

    public void removeFromCart() {
        SelenideElement button = child("button");
        button.click();
        button.shouldHave(exactText("Add to cart"));
    }
}
