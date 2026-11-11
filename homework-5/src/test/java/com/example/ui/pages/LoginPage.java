package com.example.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

/** Login page of https://www.saucedemo.com. Locators are private: tests only use business methods. */
public class LoginPage {

    private final SelenideElement usernameInput = $("[data-test='username']");
    private final SelenideElement passwordInput = $("[data-test='password']");
    private final SelenideElement loginButton = $("[data-test='login-button']");
    private final SelenideElement errorMessage = $("[data-test='error']");

    public LoginPage open() {
        Selenide.open("/");
        loginButton.shouldBe(visible);
        return this;
    }

    /** Happy path: valid credentials lead to the product catalog. */
    public InventoryPage loginAs(String username, String password) {
        submitCredentials(username, password);
        return new InventoryPage();
    }

    /** Unhappy path: login is expected to be rejected, so we stay on the login page. */
    public LoginPage loginExpectingError(String username, String password) {
        submitCredentials(username, password);
        return this;
    }

    public String errorMessage() {
        return errorMessage.shouldBe(visible).text();
    }

    private void submitCredentials(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        loginButton.click();
    }
}
