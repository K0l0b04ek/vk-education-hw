package com.example.ui.model;

import java.util.Objects;

public record Product(String name, Money price) {

    public Product {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(price, "price");
        if (name.isBlank()) {
            throw new IllegalArgumentException("product name must not be blank");
        }
    }

    @Override
    public String toString() {
        return name + " (" + price + ")";
    }
}
