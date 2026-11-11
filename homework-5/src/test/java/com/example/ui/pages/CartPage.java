package com.example.ui.pages;

import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

/** Shopping cart page. */
public class CartPage {

    private final SelenideElement title = $("[data-test='title']");

    /** The constructor waits for the page, so having an instance means we really are on this page. */
    public CartPage() {
        webdriver().shouldHave(urlContaining("/cart.html"));
        title.shouldBe(visible);
    }

    public String title() {
        return title.text();
    }

    public List<String> itemNames() {
        return $$("[data-test='inventory-item-name']").texts();
    }

    public CartPage remove(String productName) {
        SelenideElement item = $$("[data-test='inventory-item']").findBy(text(productName));
        item.$("button").click();
        item.should(disappear);
        return this;
    }
}
