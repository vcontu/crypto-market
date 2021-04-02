package com.endava.internship.cryptomarket.confservice;

import io.restassured.http.Header;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.TEXT;
import static org.hamcrest.CoreMatchers.containsString;

class HelloWorldIntegrationTest {

    private final String testUrl = "http://localhost:8080/conf-service/hello";

    @Test
    void whenAccessHelloWorldWithTextPlain_thenReturnTextPlainHelloWorldAndStatus200() {
        final Header acceptHeader = new Header("Accept", "text/plain");
        given().header(acceptHeader)
                .when().get(testUrl)
                .then().assertThat().statusCode(200).and().contentType(TEXT).and().body(containsString("Hello World!"));
    }

    @Test
    void whenAccessHelloWorldWithWrongAccept_thenReturnStatus406() {
        given().header(new Header("Accept", "text/html"))
                .when().get(testUrl)
                .then().assertThat().statusCode(406);
    }

}
