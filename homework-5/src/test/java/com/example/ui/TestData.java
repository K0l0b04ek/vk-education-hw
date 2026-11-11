package com.example.ui;

import java.math.BigDecimal;
import java.util.List;

/** Test data of the SauceDemo demo shop. */
public final class TestData {

    private TestData() {}

    public static final String STANDARD_USER = "standard_user";
    public static final String PROBLEM_USER = "problem_user";
    public static final String PASSWORD = "secret_sauce";

    public static final List<String> ALL_PRODUCTS = List.of(
            "Sauce Labs Backpack",
            "Sauce Labs Bike Light",
            "Sauce Labs Bolt T-Shirt",
            "Sauce Labs Fleece Jacket",
            "Sauce Labs Onesie",
            "Test.allTheThings() T-Shirt (Red)");

    /** All catalog prices in ascending order (two products cost 15.99). */
    public static final List<BigDecimal> ALL_PRICES_ASCENDING = List.of(
            new BigDecimal("7.99"),
            new BigDecimal("9.99"),
            new BigDecimal("15.99"),
            new BigDecimal("15.99"),
            new BigDecimal("29.99"),
            new BigDecimal("49.99"));
}
