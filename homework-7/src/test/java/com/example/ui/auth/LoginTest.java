package com.example.ui.auth;

import com.example.ui.BaseTest;
import com.example.ui.data.InvalidLoginProvider;
import com.example.ui.data.ProductRegistry;
import com.example.ui.data.UserFactory;
import com.example.ui.model.User;
import com.example.ui.pages.InventoryPage;
import com.example.ui.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Login")
@Tag("auth")
@Timeout(60)
class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeEach
    void openLoginPage() {
        loginPage = new LoginPage().get();
    }

    @Nested
    @DisplayName("with valid credentials")
    class ValidCredentials {

        @Test
        @Tag("smoke")
        @Timeout(30)
        @DisplayName("standard user lands on the full product catalog")
        void standardUserSeesCatalog() {
            InventoryPage inventory = loginPage.loginAs(UserFactory.standard());

            assertAll("catalog after login",
                    () -> assertEquals("Products", inventory.title()),
                    () -> assertEquals(ProductRegistry.all().size(), inventory.products().size(),
                            "unexpected number of products"));
        }

        @ParameterizedTest(name = "{0} can log in")
        @MethodSource("com.example.ui.auth.LoginTest#regularUsers")
        @Tag("regression")
        @DisplayName("every regular user type reaches the catalog")
        void regularUsersCanLogIn(User user) {
            InventoryPage inventory = loginPage.loginAs(user);

            assertEquals("Products", inventory.title());
        }
    }

    @Nested
    @DisplayName("with invalid credentials")
    @Tag("negative")
    class InvalidCredentials {

        @ParameterizedTest(name = "{0} -> {1}")
        @ArgumentsSource(InvalidLoginProvider.class)
        @Tag("smoke")
        @Timeout(30)
        @DisplayName("is rejected with an exact error message")
        void invalidCredentialsAreRejected(User user, String expectedError) {
            LoginPage result = loginPage.loginExpectingError(user);

            assertEquals(expectedError, result.errorMessage());
        }
    }

    static Stream<User> regularUsers() {
        return Stream.of(UserFactory.standard(), UserFactory.problem(), UserFactory.visual());
    }
}
