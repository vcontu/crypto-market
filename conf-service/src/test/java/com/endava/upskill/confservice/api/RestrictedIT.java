package com.endava.upskill.confservice.api;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_NOT_ACCEPTABLE;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class RestrictedIT {

    private static final String INSTANCE_URL = "http://localhost:8080/conf-service/restricted";

    @Test
    void whenAccessRestrictedWithAcceptTextPlainAndUserAdmin_thenReturnTextPlainAccessGrantedAndStatus200() {
        given().headers(CustomHeaders.USERNAME, "admin")
                .accept(ContentType.TEXT)

                .when().get(INSTANCE_URL)

                .then().assertThat()
                .contentType(ContentType.TEXT)
                .statusCode(SC_OK)
                .body(containsString("Access Granted for user: admin"));
    }

    @Test
    void whenAccessRestrictedWithAcceptTextPlainAndUserNonAdmin_thenReturnMessageAccessDeniedStatus403() {
        given().headers(CustomHeaders.USERNAME, "unknown")
                .accept(ContentType.TEXT)

                .when().get(INSTANCE_URL)

                .then().assertThat()
                .contentType(ContentType.TEXT)
                .statusCode(SC_FORBIDDEN)
                .body(containsString("Access Denied for user: unknown"));
    }

    @Test
    void whenAccessRestrictedWithWrongAcceptAndUserAdmin_thenReturnStatus406() {
        given().headers(CustomHeaders.USERNAME, "admin")
                .accept(ContentType.HTML)

                .when().get(INSTANCE_URL)

                .then().assertThat()
                .statusCode(SC_NOT_ACCEPTABLE);
    }
}
