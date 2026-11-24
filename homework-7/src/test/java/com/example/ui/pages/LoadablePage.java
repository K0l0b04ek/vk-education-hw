package com.example.ui.pages;

import com.codeborne.selenide.WebDriverRunner;

public abstract class LoadablePage<T extends LoadablePage<T>> {

    public T get() {
        if (!isLoaded()) {
            load();
        }
        verifyLoaded();
        return self();
    }

    public T await() {
        verifyLoaded();
        return self();
    }

    protected abstract boolean isLoaded();

    protected abstract void load();

    protected abstract void verifyLoaded();

    protected abstract T self();

    protected static boolean browserIsOn(String urlPart) {
        return WebDriverRunner.hasWebDriverStarted() && WebDriverRunner.url().contains(urlPart);
    }
}
