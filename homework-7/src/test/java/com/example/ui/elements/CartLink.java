package com.example.ui.elements;

import com.codeborne.selenide.SelenideElement;
import com.example.ui.locators.Locators;
import org.openqa.selenium.By;

public class CartLink extends PageElement {

    private static final By BADGE = Locators.testId("shopping-cart-badge");

    public CartLink(SelenideElement root) {
        super(root);
    }

    public int itemCount() {
        SelenideElement badge = child(BADGE);
        return badge.exists() ? Integer.parseInt(badge.text()) : 0;
    }

    public void open() {
        root().click();
    }
}
