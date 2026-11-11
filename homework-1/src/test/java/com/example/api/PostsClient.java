package com.example.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

/** API client: tests do not deal with HTTP details directly. */
public class PostsClient {

    // ---- posts ----
    public Response getAll() {
        return given(Specs.request()).get("/posts");
    }

    public Response getById(Object id) {
        return given(Specs.request()).get("/posts/{id}", id);
    }

    public Response getByUser(Object userId) {
        return given(Specs.request()).queryParam("userId", userId).get("/posts");
    }

    public Response getPage(int page, int limit) {
        return given(Specs.request())
                .queryParam("_page", page)
                .queryParam("_limit", limit)
                .get("/posts");
    }

    public Response getComments(int postId) {
        return given(Specs.request()).get("/posts/{id}/comments", postId);
    }

    public Response create(Map<String, Object> body) {
        return given(Specs.request())
                .contentType(ContentType.JSON)
                .body(body)
                .post("/posts");
    }

    /** Sends an arbitrary body with an arbitrary Content-Type (for negative cases). */
    public Response createRaw(String rawBody, String contentType) {
        return given(Specs.request())
                .contentType(contentType)
                .body(rawBody)
                .post("/posts");
    }

    public Response update(int id, Map<String, Object> body) {
        return given(Specs.request())
                .contentType(ContentType.JSON)
                .body(body)
                .put("/posts/{id}", id);
    }

    public Response patch(int id, Map<String, Object> body) {
        return given(Specs.request())
                .contentType(ContentType.JSON)
                .body(body)
                .patch("/posts/{id}", id);
    }

    public Response delete(int id) {
        return given(Specs.request()).delete("/posts/{id}", id);
    }

    // ---- other ----
    public Response get(String path) {
        return given(Specs.request()).get(path);
    }

    public Response getUsers() {
        return given(Specs.request()).get("/users");
    }
}
