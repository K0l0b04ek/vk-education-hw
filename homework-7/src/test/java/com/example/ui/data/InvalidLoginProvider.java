package com.example.ui.data;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class InvalidLoginProvider implements ArgumentsProvider {

    private static final String NOT_MATCHING =
            "Epic sadface: Username and password do not match any user in this service";

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(UserFactory.withEmptyUsername(), "Epic sadface: Username is required"),
                Arguments.of(UserFactory.withEmptyPassword(), "Epic sadface: Password is required"),
                Arguments.of(UserFactory.withWrongPassword(), NOT_MATCHING),
                Arguments.of(UserFactory.unknown(), NOT_MATCHING),
                Arguments.of(UserFactory.lockedOut(), "Epic sadface: Sorry, this user has been locked out."));
    }
}
