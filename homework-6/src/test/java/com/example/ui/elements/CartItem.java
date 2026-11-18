package com.example.ui.elements;

import com.codeborne.selenide.SelenideElement;
import com.example.ui.model.Money;
import com.example.ui.model.Product;

import static com.codeborne.selenide.Condition.disappear;

public class CartItem extends PageElement {

    public CartItem(SelenideElement root) {
        super(root);
    }

    public Product toProduct() {
        String name = child("[data-test='inventory-item-name']").text();
        Money price = Money.parse(child("[data-test='inventory-item-price']").text());
        return new Product(name, price);
    }

    public void remove() {
        child("button").click();
        root().should(disappear);
    }
}
