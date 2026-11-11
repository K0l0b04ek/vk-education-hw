package com.example.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.lessThan;

/** Shared request/response specifications: baseUri is configured in one place. */
public final class Specs {

    private Specs() {}

    public static RequestSpecification request() {
        return new RequestSpecBuilder()
                .setBaseUri(System.getProperty("baseUrl", "https://jsonplaceholder.typicode.com"))
                .setAccept(ContentType.JSON)
                .log(LogDetail.URI)
                .build();
    }

    /** Common check: response time is under 3 seconds. */
    public static ResponseSpecification fast() {
        return new ResponseSpecBuilder()
                .expectResponseTime(lessThan(3000L))
                .build();
    }
}
