package com.example.ui.data;

import com.example.ui.model.Money;
import com.example.ui.model.Product;

import java.util.List;

public final class ProductRegistry {

    private ProductRegistry() {}

    public static final Product BACKPACK = new Product("Sauce Labs Backpack", Money.of("29.99"));
    public static final Product BIKE_LIGHT = new Product("Sauce Labs Bike Light", Money.of("9.99"));
    public static final Product BOLT_T_SHIRT = new Product("Sauce Labs Bolt T-Shirt", Money.of("15.99"));
    public static final Product FLEECE_JACKET = new Product("Sauce Labs Fleece Jacket", Money.of("49.99"));
    public static final Product ONESIE = new Product("Sauce Labs Onesie", Money.of("7.99"));
    public static final Product RED_T_SHIRT = new Product("Test.allTheThings() T-Shirt (Red)", Money.of("15.99"));

    private static final List<Product> ALL = List.of(
            BACKPACK, BIKE_LIGHT, BOLT_T_SHIRT, FLEECE_JACKET, ONESIE, RED_T_SHIRT);

    public static List<Product> all() {
        return ALL;
    }
}
