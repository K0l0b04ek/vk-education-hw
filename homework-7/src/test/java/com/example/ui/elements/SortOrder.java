package com.example.ui.elements;

public enum SortOrder {
    NAME_A_TO_Z("az"),
    NAME_Z_TO_A("za"),
    PRICE_LOW_TO_HIGH("lohi"),
    PRICE_HIGH_TO_LOW("hilo");

    private final String optionValue;

    SortOrder(String optionValue) {
        this.optionValue = optionValue;
    }

    public String optionValue() {
        return optionValue;
    }
}
