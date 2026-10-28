package com.example.api;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/** Boundary values and equivalence classes. */
class PostsBoundaryTest {

    private final PostsClient api = new PostsClient();

    @ParameterizedTest(name = "GET /posts/{0} -> {1}")
    @CsvSource({
            "-1,  404",
            "0,   404",   // below the lower bound
            "1,   200",   // lower bound
            "2,   200",
            "99,  200",
            "100, 200",   // upper bound
            "101, 404"    // above the upper bound
    })
    @Tag("regression")
    @DisplayName("Boundary values of id (the database has 100 posts)")
    void idBoundaries(int id, int expectedStatus) {
        api.getById(id).then().statusCode(expectedStatus);
    }

    @ParameterizedTest(name = "_limit={0} -> {1} posts returned")
    @CsvSource({
            "1,   1",
            "2,   2",
            "10,  10",
            "99,  99",
            "100, 100",
            "101, 100"   // more than exists: everything is returned
    })
    @Tag("regression")
    @DisplayName("Pagination boundaries: page size")
    void pageSizeBoundaries(int limit, int expectedSize) {
        api.getPage(1, limit).then()
                .statusCode(200)
                .body("size()", equalTo(expectedSize));
    }

    @ParameterizedTest(name = "_page={0}, _limit=10 -> {1} posts")
    @CsvSource({
            "1,  10",    // first page
            "10, 10",    // last page
            "11, 0"      // beyond the last page
    })
    @Tag("regression")
    @DisplayName("Pagination boundaries: page number")
    void pageNumberBoundaries(int page, int expectedSize) {
        api.getPage(page, 10).then()
                .statusCode(200)
                .body("size()", equalTo(expectedSize));
    }

    @ParameterizedTest(name = "POST: title of {0} characters is accepted")
    @ValueSource(ints = {0, 1, 255, 256, 10_000})
    @Tag("regression")
    @DisplayName("Title length: empty, 1, 255, 256, 10000 characters (API has no limit)")
    void titleLengths(int length) {
        String title = "a".repeat(length);

        api.create(Map.of("title", title, "body", "b", "userId", 1)).then()
                .statusCode(201)
                .body("title", equalTo(title));
    }

    @ParameterizedTest(name = "POST: special characters ''{0}'' are stored unchanged")
    @ValueSource(strings = {
            "Привет, мир",
            "日本語のタイトル",
            "emoji \uD83D\uDE00",
            "<script>alert(1)</script>",
            "'; DROP TABLE posts; --",
            "quote \" and backslash \\"
    })
    @Tag("regression")
    @DisplayName("Unicode and potentially dangerous strings in title")
    void specialCharactersInTitle(String title) {
        api.create(Map.of("title", title, "body", "b", "userId", 1)).then()
                .statusCode(201)
                .body("title", equalTo(title));
    }
}
