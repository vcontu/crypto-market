package com.endava.internship.cryptomarket.confservice;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.TEXT;
import static org.hamcrest.CoreMatchers.containsString;

class RestrictedIntegrationTest {

    private final String testUrl = "http://localhost:8080/conf-service/restricted";

    @Test
    void whenAccessRestrictedWithAcceptTextPlainAndUserAdmin_thenReturnTextPlainAccessGrantedAndStatus200() {
        final Headers getHeaders = new Headers(new Header("Accept", "text/plain"),
                                    new Header("username", "admin"));
        given().headers(getHeaders)
                .when().get(testUrl)
                .then().assertThat().contentType(TEXT).statusCode(200).body(containsString("Access Granted for user: admin"));
    }

    @Test
    void whenAccessRestrictedWithAcceptTextPlainAndUserNonAdmin_thenReturnMessageAccessDeniedStatus403() {
        final Headers getHeaders = new Headers(new Header("Accept", "text/plain"),
                                    new Header("username", "unknown"));
        given().headers(getHeaders)
                .when().get(testUrl)
                .then().assertThat().contentType(TEXT).and()
                .body(containsString("Access Denied for user: unknown")).and().statusCode(403);
    }

    @Test
    void whenAccessRestrictedWithWrongAcceptAndUserAdmin_thenReturnStatus406() {
        final Headers getHeaders = new Headers(new Header("Accept", "text/html"), new Header("username", "admin"));
        given().headers(getHeaders)
                .when().get(testUrl)
                .then().assertThat().statusCode(406);
    }

}
