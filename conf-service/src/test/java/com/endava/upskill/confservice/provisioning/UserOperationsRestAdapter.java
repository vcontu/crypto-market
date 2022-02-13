package com.endava.upskill.confservice.provisioning;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.extension.ExtensionContext;

import com.endava.upskill.confservice.domain.model.create.UserDto;
import com.endava.upskill.confservice.util.Tokens;

import static com.endava.upskill.confservice.util.Endpoint.CREATE_USER;
import static com.endava.upskill.confservice.util.Endpoint.DELETE_USER;
import static com.endava.upskill.confservice.util.ResponseValidationSpecs.buildRequestSpec;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class UserOperationsRestAdapter implements UserOperations {

    public UserOperationsRestAdapter() {
        RestAssured.baseURI = "http://localhost/conf-service";
        RestAssured.port = 8080;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Override
    public void createUser(UserDto user, LocalDateTime localDateTime, String requesterUsername, ExtensionContext extensionContext) {
        given()
                .header(Tokens.REQUESTER_HEADER, requesterUsername)
                .spec(buildRequestSpec(user))

                .when().post(CREATE_USER.getPath())

                .then()
                .statusCode(HttpServletResponse.SC_CREATED);
    }

    @Override
    public void deleteUser(String username, String requesterUsername, ExtensionContext extensionContext) {
        given()
                .headers(Tokens.REQUESTER_ADMIN)

                .when().delete(DELETE_USER.getPath(), username)

                .then()
                .statusCode(HttpServletResponse.SC_NO_CONTENT);
    }
}
