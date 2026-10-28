package com.example.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Negative scenarios. */
class PostsNegativeTest {

    private final PostsClient api = new PostsClient();

    @ParameterizedTest(name = "GET /posts/{0} -> 404")
    @ValueSource(strings = {"99999", "abc", "-1", "0", "1.5"})
    @Tag("smoke")
    @DisplayName("Invalid or non-existent id -> 404")
    void invalidId(String id) {
        api.getById(id).then().statusCode(404);
    }

    @Test
    @Tag("smoke")
    @DisplayName("Unknown path -> 404")
    void unknownPath() {
        api.get("/this-resource-does-not-exist").then().statusCode(404);
    }

    @Test
    @Tag("regression")
    @DisplayName("Filter by non-existent userId -> 200 and empty list (not an error)")
    void filterByUnknownUser() {
        api.getByUser(999).then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }

    @Test
    @Tag("regression")
    @DisplayName("Filter by non-numeric userId -> empty list, not 5xx")
    void filterByInvalidUser() {
        api.getByUser("abc").then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }

    @Test
    @Tag("regression")
    @DisplayName("Comments of a non-existent post -> empty list")
    void commentsOfUnknownPost() {
        api.getComments(99999).then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }

    @Test
    @Tag("regression")
    @DisplayName("404 response does not leak stack trace or internal details")
    void notFoundDoesNotLeakInternals() {
        String body = api.getById(99999).then().statusCode(404).extract().asString();

        assertThat(body, not(containsStringIgnoringCase("exception")));
        assertThat(body, not(containsStringIgnoringCase("at java.")));
        assertThat(body, not(containsStringIgnoringCase("stacktrace")));
    }

    /**
     * JSONPlaceholder is a fake API without server-side validation: an empty JSON is accepted.
     */
    @Test
    @Tag("regression")
    @DisplayName("POST with empty JSON {} -> API does not validate input (documents actual behavior)")
    void createWithEmptyJson() {
        api.createRaw("{}", "application/json").then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Tag("regression")
    @DisplayName("POST: extra unknown field does not break the request and does not cause 5xx")
    void createWithUnknownField() {
        api.createRaw("{\"title\":\"t\",\"body\":\"b\",\"userId\":1,\"isAdmin\":true}", "application/json")
                .then()
                .statusCode(lessThan(500));
    }
}
