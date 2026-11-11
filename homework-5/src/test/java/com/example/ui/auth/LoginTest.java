package com.example.ui.auth;

import com.example.ui.BaseTest;
import com.example.ui.pages.InventoryPage;
import com.example.ui.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;

import static com.example.ui.TestData.ALL_PRODUCTS;
import static com.example.ui.TestData.PASSWORD;
import static com.example.ui.TestData.STANDARD_USER;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Login")
@Tag("auth")
@Timeout(60)
class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeEach
    void openLoginPage() {
        loginPage = new LoginPage().open();
    }

    @Nested
    @DisplayName("with valid credentials")
    class ValidCredentials {

        @Test
        @Tag("smoke")
        @Timeout(30)
        @DisplayName("standard user sees the full product catalog")
        void standardUserSeesFullCatalog() {
            InventoryPage inventory = loginPage.loginAs(STANDARD_USER, PASSWORD);
            List<String> names = inventory.productNames();

            assertAll("product catalog after login",
                    () -> assertEquals("Products", inventory.title()),
                    () -> assertEquals(ALL_PRODUCTS.size(), names.size(), "unexpected number of products"),
                    () -> assertEquals(Set.copyOf(ALL_PRODUCTS), Set.copyOf(names), "unexpected set of products"));
        }

        @ParameterizedTest(name = "{0} can log in")
        @ValueSource(strings = {"standard_user", "problem_user", "visual_user"})
        @Tag("regression")
        @DisplayName("every regular user type reaches the catalog")
        void regularUsersCanLogIn(String username) {
            InventoryPage inventory = loginPage.loginAs(username, PASSWORD);

            assertEquals("Products", inventory.title());
        }
    }

    @Nested
    @DisplayName("with invalid credentials")
    @Tag("negative")
    class InvalidCredentials {

        @ParameterizedTest(name = "user ''{0}'' / password ''{1}'' -> {2}")
        @CsvSource(delimiter = '|', value = {
                "''             | secret_sauce   | Epic sadface: Username is required",
                "standard_user  | ''             | Epic sadface: Password is required",
                "standard_user  | wrong_password | Epic sadface: Username and password do not match any user in this service",
                "unknown_user   | secret_sauce   | Epic sadface: Username and password do not match any user in this service",
                "locked_out_user | secret_sauce  | Epic sadface: Sorry, this user has been locked out."
        })
        @Tag("smoke")
        @Timeout(30)
        @DisplayName("is rejected with an exact error message")
        void invalidCredentialsAreRejected(String username, String password, String expectedError) {
            LoginPage result = loginPage.loginExpectingError(username, password);

            assertEquals(expectedError, result.errorMessage());
        }
    }
}
