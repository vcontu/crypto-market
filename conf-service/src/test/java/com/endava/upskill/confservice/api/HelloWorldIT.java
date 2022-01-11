package com.endava.upskill.confservice.api;

import static javax.servlet.http.HttpServletResponse.SC_NOT_ACCEPTABLE;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class HelloWorldIT {

    private static final String INSTANCE_URL = "http://localhost:8080/conf-service/hello";

    @Test
    void whenAccessHelloWorldWithTextPlain_thenReturnTextPlainHelloWorldAndStatus200() {
        given().accept(ContentType.TEXT)

                .when().get(INSTANCE_URL)

                .then().assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.TEXT)
                .body(containsString("Hello World!"));
    }

    @Test
    void whenAccessHelloWorldWithWrongAccept_thenReturnStatus406() {
        given().accept(ContentType.HTML)

                .when().get(INSTANCE_URL)

                .then().assertThat()
                .statusCode(SC_NOT_ACCEPTABLE);
    }
}
