package com.example.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.example.ui.pages.InventoryPage;
import com.example.ui.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Map;

/** Common browser setup. Each test gets a fresh browser, so tests do not share cart/login state. */
public abstract class BaseTest {

    @BeforeAll
    protected static void configureBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        // turn off Chrome password manager / leak-detection popups that can cover the page
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        Configuration.browserCapabilities = options;
    }

    @AfterEach
    protected void closeBrowser() {
        startFreshBrowser();
    }

    /** Closes the current browser; Selenide opens a new clean one on the next open(). */
    protected static void startFreshBrowser() {
        Selenide.closeWebDriver();
    }

    protected static InventoryPage openCatalogAsStandardUser() {
        return new LoginPage().open().loginAs(TestData.STANDARD_USER, TestData.PASSWORD);
    }
}
