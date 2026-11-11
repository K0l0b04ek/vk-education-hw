package com.example.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/** Response validation: schema, values, data consistency. */
class PostsValidationTest {

    private final PostsClient api = new PostsClient();

    @Test
    @Tag("smoke")
    @DisplayName("Single post matches the JSON Schema")
    void postMatchesJsonSchema() {
        api.getById(1).then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/post.json"));
    }

    @Test
    @Tag("smoke")
    @DisplayName("List of posts matches the JSON Schema")
    void postsListMatchesJsonSchema() {
        api.getAll().then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/posts.json"));
    }

    @Test
    @Tag("regression")
    @DisplayName("Comments match the JSON Schema (including email format)")
    void commentsMatchJsonSchema() {
        api.getComments(1).then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/comment.json"));
    }

    @Test
    @Tag("regression")
    @DisplayName("Comments of post 1: postId matches, email is valid")
    void commentsAreValid() {
        api.getComments(1).then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("postId", everyItem(equalTo(1)))
                .body("email", everyItem(matchesPattern("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")));
    }

    @Test
    @Tag("regression")
    @DisplayName("Consistency: every post has an existing author in /users")
    void everyPostHasExistingAuthor() {
        List<Integer> userIds = api.getUsers().then().statusCode(200)
                .extract().jsonPath().getList("id", Integer.class);
        List<Integer> authorIds = api.getAll().then().statusCode(200)
                .extract().jsonPath().getList("userId", Integer.class);

        assertThat("some posts reference a non-existent author",
                userIds, hasItems(authorIds.stream().distinct().toArray(Integer[]::new)));
    }

    @Test
    @Tag("regression")
    @DisplayName("Pagination: walking through all pages gives 100 posts without duplicates")
    void paginationHasNoDuplicatesAndNoGaps() {
        List<Integer> all = new ArrayList<>();
        for (int page = 1; page <= 10; page++) {
            all.addAll(api.getPage(page, 10).then().statusCode(200)
                    .extract().jsonPath().getList("id", Integer.class));
        }

        Set<Integer> unique = new HashSet<>(all);
        assertThat("duplicates found between pages", unique.size(), equalTo(all.size()));
        assertThat("posts were lost while walking through pages", all.size(), equalTo(100));
    }

    @Test
    @Tag("regression")
    @DisplayName("Sum of posts per user equals the total number of posts")
    void postsByUsersSumToTotal() {
        int total = api.getAll().then().extract().jsonPath().getList("id").size();

        int sum = 0;
        for (int userId = 1; userId <= 10; userId++) {
            sum += api.getByUser(userId).then().statusCode(200)
                    .extract().jsonPath().getList("id").size();
        }

        assertThat(sum, equalTo(total));
    }
}
