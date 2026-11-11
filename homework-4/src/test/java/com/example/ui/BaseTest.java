package com.example.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Map;

abstract class BaseTest {

    @BeforeAll
    static void configureBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        Configuration.browserCapabilities = options;
    }

    @AfterEach
    void closeBrowser() {
        Selenide.closeWebDriver();
    }
}
