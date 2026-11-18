package com.example.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.example.ui.model.User;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

/** Login page of https://www.saucedemo.com. Locators are private: tests only use business methods. */
public class LoginPage extends LoadablePage<LoginPage> {

    private final SelenideElement usernameInput = $("[data-test='username']");
    private final SelenideElement passwordInput = $("[data-test='password']");
    private final SelenideElement loginButton = $("[data-test='login-button']");
    private final SelenideElement errorMessage = $("[data-test='error']");

    /** Happy path: valid credentials lead to the product catalog. */
    public InventoryPage loginAs(User user) {
        submit(user);
        return new InventoryPage().await();
    }

    /** Unhappy path: login is expected to be rejected, so we stay on the login page. */
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
