package com.example.ui.elements;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public abstract class PageElement {

    private final SelenideElement root;

    protected PageElement(SelenideElement root) {
        this.root = root;
    }

    protected SelenideElement root() {
        return root;
    }

    protected SelenideElement child(By locator) {
        return root.$(locator);
    }
}
