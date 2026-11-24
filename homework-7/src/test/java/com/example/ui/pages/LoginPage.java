package com.example.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.example.ui.locators.Locators;
import com.example.ui.model.User;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends LoadablePage<LoginPage> {

    private final SelenideElement usernameInput = $(Locators.testId("username"));
    private final SelenideElement passwordInput = $(Locators.testId("password"));
    private final SelenideElement loginButton = $(Locators.testId("login-button"));
    private final SelenideElement errorMessage = $(Locators.testId("error"));

    public InventoryPage loginAs(User user) {
        submit(user);
        return new InventoryPage().await();
    }

    public LoginPage loginExpectingError(User user) {
        submit(user);
        return this;
    }

    public String errorMessage() {
        return errorMessage.shouldBe(visible).text();
    }

    private void submit(User user) {
        usernameInput.setValue(user.username());
        passwordInput.setValue(user.password());
        loginButton.click();
    }

    @Override
    protected boolean isLoaded() {
        return WebDriverRunner.hasWebDriverStarted() && loginButton.is(visible);
    }

    @Override
    protected void load() {
        Selenide.open("/");
    }

    @Override
    protected void verifyLoaded() {
        loginButton.shouldBe(visible);
    }

    @Override
    protected LoginPage self() {
        return this;
    }
}
