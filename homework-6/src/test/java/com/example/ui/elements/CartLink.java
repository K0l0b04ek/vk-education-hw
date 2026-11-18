package com.example.ui.elements;

import com.codeborne.selenide.SelenideElement;

public class CartLink extends PageElement {

    public CartLink(SelenideElement root) {
        super(root);
    }

    public int itemCount() {
        SelenideElement badge = child("[data-test='shopping-cart-badge']");
        return badge.exists() ? Integer.parseInt(badge.text()) : 0;
    }

    public void open() {
        root().click();
    }
}
