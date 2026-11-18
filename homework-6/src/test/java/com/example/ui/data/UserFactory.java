package com.example.ui.data;

import com.example.ui.model.User;

public final class UserFactory {

    private static final String PASSWORD = "secret_sauce";

    private UserFactory() {}

    public static User standard() {
        return new User("standard_user", PASSWORD);
    }

    public static User problem() {
        return new User("problem_user", PASSWORD);
    }

    public static User visual() {
        return new User("visual_user", PASSWORD);
    }

    public static User lockedOut() {
        return new User("locked_out_user", PASSWORD);
    }

    public static User unknown() {
        return new User("unknown_user", PASSWORD);
    }

    public static User withWrongPassword() {
        return new User(standard().username(), "wrong_password");
    }

    public static User withEmptyUsername() {
        return new User("", PASSWORD);
    }

    public static User withEmptyPassword() {
        return new User(standard().username(), "");
    }
}
