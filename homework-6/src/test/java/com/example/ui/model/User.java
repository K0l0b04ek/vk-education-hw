package com.example.ui.model;

import java.util.Objects;

public record User(String username, String password) {

    public User {
        Objects.requireNonNull(username, "username");
        Objects.requireNonNull(password, "password");
    }

    @Override
    public String toString() {
        return "User[" + username + "]";
    }
}
