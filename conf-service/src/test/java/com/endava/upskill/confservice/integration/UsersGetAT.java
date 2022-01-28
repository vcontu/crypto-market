package com.endava.upskill.confservice.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.endava.upskill.confservice.util.Endpoint;
import com.endava.upskill.confservice.util.ResponseValidationSpecs;
import com.endava.upskill.confservice.util.Tokens;

import static io.restassured.RestAssured.given;

@DisplayName("GET /users/{username}")
public class UsersGetAT extends ResponseValidationSpecs {

    private static final String GET_USER_PATH = Endpoint.GET_USER.getPath();

    @Test
    @DisplayName("User not found")
    void whenUserDoesNotExist_respondUserNotFound() {
        final ExceptionResponse exceptionResponse = ExceptionResponse.USER_NOT_FOUND;

        given()
                .headers(Tokens.REQUESTER_ADMIN)

                .when()
                .get(GET_USER_PATH, Tokens.USERNAME_NOT_EXISTING)

                .then()
                .spec(buildResponseSpec(exceptionResponse, Tokens.USERNAME_NOT_EXISTING))
                .statusCode(exceptionResponse.getStatusCode());
    }
}
