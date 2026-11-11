package com.example.api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/** Positive CRUD scenarios for /posts. */
class PostsPositiveTest {

    private final PostsClient api = new PostsClient();

    @Test
    @Tag("smoke")
    @DisplayName("GET /posts/1 -> 200, correct fields, fast response")
    void getPostById() {
        api.getById(1).then()
                .spec(Specs.fast())
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", not(emptyOrNullString()))
                .body("body", not(emptyOrNullString()));
    }

    @Test
    @Tag("smoke")
    @DisplayName("GET /posts -> 100 posts, ids are unique")
    void getAllPosts() {
        api.getAll().then()
                .statusCode(200)
                .body("size()", equalTo(100))
                .body("id", everyItem(greaterThan(0)))
                .body("id.unique().size()", equalTo(100));
    }

    @Test
    @Tag("regression")
    @DisplayName("GET /posts?userId=1 -> only posts of user 1")
    void filterByUser() {
        api.getByUser(1).then()
                .statusCode(200)
                .body("size()", equalTo(10))
                .body("userId", everyItem(equalTo(1)));
    }

    @Test
    @Tag("smoke")
    @DisplayName("POST /posts -> 201, id assigned, data echoed back")
    void createPost() {
        api.create(Map.of("title", "My title", "body", "My body", "userId", 7)).then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("id", greaterThan(100))
                .body("title", equalTo("My title"))
                .body("body", equalTo("My body"))
                .body("userId", equalTo(7));
    }

    @Test
    @Tag("regression")
    @DisplayName("PUT /posts/1 -> 200, fields replaced")
    void updatePost() {
        api.update(1, Map.of("id", 1, "title", "Updated", "body", "New body", "userId", 1)).then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("Updated"))
                .body("body", equalTo("New body"));
    }

    @Test
    @Tag("regression")
    @DisplayName("PATCH /posts/1 -> 200, only title changes, other fields preserved")
    void patchPost() {
        api.patch(1, Map.of("title", "Patched")).then()
                .statusCode(200)
                .body("title", equalTo("Patched"))
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("body", not(emptyOrNullString()));
    }

    @Test
    @Tag("regression")
    @DisplayName("DELETE /posts/1 -> 200")
    void deletePost() {
        api.delete(1).then().statusCode(200);
    }

    // ---- HTTP method properties ----

    @Test
    @Tag("regression")
    @DisplayName("PUT is idempotent: two identical requests give the same result")
    void putIsIdempotent() {
        Map<String, Object> body = Map.of("id", 1, "title", "Same", "body", "Same", "userId", 1);

        String first = api.update(1, body).then().statusCode(200).extract().asString();
        String second = api.update(1, body).then().statusCode(200).extract().asString();

        assertThat("repeated PUT changed the result", second, equalTo(first));
    }

    @Test
    @Tag("regression")
    @DisplayName("GET is safe: repeated reads return the same data")
    void getIsSafe() {
        String first = api.getById(1).then().statusCode(200).extract().asString();
        String second = api.getById(1).then().statusCode(200).extract().asString();

        assertThat("GET changed the state of the resource", second, equalTo(first));
    }

    @Test
    @Tag("regression")
    @DisplayName("Response headers: Content-Type contains application/json")
    void responseHeaders() {
        api.getById(1).then()
                .statusCode(200)
                .header("Content-Type", containsString("application/json"));
    }
}
