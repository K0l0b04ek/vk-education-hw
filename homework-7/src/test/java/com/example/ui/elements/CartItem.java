package com.example.ui.elements;

import com.codeborne.selenide.SelenideElement;
import com.example.ui.locators.Locators;
import com.example.ui.model.Money;
import com.example.ui.model.Product;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.disappear;

public class CartItem extends PageElement {

    private static final By NAME = Locators.testId("inventory-item-name");
    private static final By PRICE = Locators.testId("inventory-item-price");
    private static final By REMOVE_BUTTON = Locators.testIdStartsWith("remove");

    public CartItem(SelenideElement root) {
        super(root);
    }

    public Product toProduct() {
        return new Product(child(NAME).text(), Money.parse(child(PRICE).text()));
    }

    public void remove() {
        child(REMOVE_BUTTON).click();
        root().should(disappear);
    }
}
