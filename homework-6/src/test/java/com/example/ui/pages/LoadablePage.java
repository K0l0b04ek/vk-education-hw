package com.example.ui.pages;

import com.codeborne.selenide.WebDriverRunner;

/**
 * Loadable Component: a page knows how to check that it is loaded and how to load itself.
 *
 * <ul>
 *   <li>{@link #get()} - "give me this page": loads it only if it is not displayed yet;</li>
 *   <li>{@link #await()} - for transitions caused by a click: just wait until the page is displayed.</li>
 * </ul>
 */
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

    /** Instant check without waiting. */
    protected abstract boolean isLoaded();

    /** Navigates to the page. */
    protected abstract void load();

    /** Waiting assertions that prove the page is displayed. */
    protected abstract void verifyLoaded();

    protected abstract T self();

    protected static boolean browserIsOn(String urlPart) {
        return WebDriverRunner.hasWebDriverStarted() && WebDriverRunner.url().contains(urlPart);
    }
}
