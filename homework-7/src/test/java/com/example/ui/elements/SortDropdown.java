package com.example.ui.elements;

import com.codeborne.selenide.SelenideElement;

public class SortDropdown extends PageElement {

    public SortDropdown(SelenideElement root) {
        super(root);
    }

    public void select(SortOrder order) {
        root().selectOptionByValue(order.optionValue());
    }
}
