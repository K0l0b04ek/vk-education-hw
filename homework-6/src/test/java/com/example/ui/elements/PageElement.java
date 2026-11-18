package com.example.ui.elements;

import com.codeborne.selenide.SelenideElement;

public abstract class PageElement {

    private final SelenideElement root;

    protected PageElement(SelenideElement root) {
        this.root = root;
    }

    protected SelenideElement root() {
        return root;
    }

    protected SelenideElement child(String css) {
        return root.$(css);
    }
}
