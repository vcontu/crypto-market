package com.endava.upskill.confservice.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.endava.upskill.confservice.domain.model.create.UserDto;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.endava.upskill.confservice.provisioning.UserOnboarding;
import com.endava.upskill.confservice.util.Endpoint;
import com.endava.upskill.confservice.util.ResponseValidationSpecs;
import com.endava.upskill.confservice.util.Tokens;

import static com.endava.upskill.confservice.domain.model.entity.Status.ACTIVE;
import static com.endava.upskill.confservice.domain.model.entity.Status.INACTV;
import static com.endava.upskill.confservice.domain.model.exception.ExceptionResponse.*;
import static com.endava.upskill.confservice.provisioning.UserOnboarding.randomUsername;

import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import lombok.RequiredArgsConstructor;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("POST /users")
public class UsersCreateAT extends ResponseValidationSpecs {

    private static final String CREATE_USER_PATH = Endpoint.CREATE_USER.getPath();

    @Test
    @DisplayName("Wrong Content-Type header: Not Acceptable")
    void givenWrongContentType_respondNotAcceptable() {
        final ExceptionResponse exceptionResponse = NOT_ACCEPTABLE_CONTENT;

        given()
                .headers(Tokens.REQUESTER_ADMIN)
                .contentType("text/plain")
                .body(UserOnboarding.randomUser(), ObjectMapperType.JACKSON_2)

                .when()
                .post(CREATE_USER_PATH)

                .then()
                .spec(buildResponseSpec(exceptionResponse))
                .statusCode(exceptionResponse.getStatusCode());
    }

    @Test
    @DisplayName("Bad request object: Request Object Validation Failure")
    void givenRequestObjectWithBadMapping_respondRequestObjectFailure() {
        final ExceptionResponse exceptionResponse = REQUEST_DESERIALIZATION;

        given()
                .headers(Tokens.REQUESTER_ADMIN)
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "username": "userhello",
                            "email":    "username06@gmail.com",
                            "status":   "NOT-EXISTING"
                        }""")

                .when()
                .post(CREATE_USER_PATH)

                .then()
                .spec(buildResponseSpec(exceptionResponse))
                .statusCode(exceptionResponse.getStatusCode());
    }

    @ParameterizedTest
    @EnumSource(CreateUserTest.class)
    @DisplayName("Request object with invalid properties: Validation Failures")
    void givenCreateUserWithInvalidRequestObject_respondWithException(CreateUserTest test) {
        given()
                .headers(Tokens.REQUESTER_ADMIN)
                .contentType(ContentType.JSON)
                .body(test.requestObject)

                .when()
                .post(CREATE_USER_PATH)

                .then()
                .spec(buildResponseSpec(test.exceptionResponse))
                .statusCode(test.exceptionResponse.getStatusCode());
    }

    @RequiredArgsConstructor
    private enum CreateUserTest {
        JsonMalformed(JSON_MALFORMED, "Corrupted JSON"),
        UsernameValidationFailure(USER_VALIDATION_USERNAME, new UserDto(null, Tokens.EMAIL, ACTIVE)),
        UsernameNullValidationFailure(USER_VALIDATION_USERNAME, new UserDto("I_va__lid", Tokens.EMAIL, ACTIVE)),
        EmailValidationFailure(USER_VALIDATION_EMAIL, new UserDto(randomUsername(), "inv__3!@$%^&&*mail", ACTIVE)),
        EmailNullValidationFailureNull(USER_VALIDATION_EMAIL, new UserDto(randomUsername(), null, ACTIVE)),
        StatusNullValidationFailure(USER_VALIDATION_STATUS, new UserDto(randomUsername(), Tokens.EMAIL, null)),
        StatusValidationFailure(USER_VALIDATION_STATUS, new UserDto(randomUsername(), Tokens.EMAIL, INACTV));

        private final ExceptionResponse exceptionResponse;

        private final Object requestObject;
    }

    @Nested
    class GivenOnboardedUserTest {

        @RegisterExtension
        private final UserOnboarding onboarding = new UserOnboarding();

        @Test
        @DisplayName("Create user with Requester non-admin: Authorization Failure")
        void whenRequesterNotAdmin_respondForbidden() {
            final ExceptionResponse exceptionResponse = AUTHORIZATION_FAILED;

            given()
                    .headers(onboarding.buildRequesterHeaders())
                    .contentType(ContentType.JSON)
                    .body(UserOnboarding.randomUser())

                    .when()
                    .post(CREATE_USER_PATH)

                    .then()
                    .spec(buildResponseSpec(exceptionResponse, onboarding.getUsername()))
                    .statusCode(exceptionResponse.getStatusCode());
        }

        @Test
        @DisplayName("Create same user again: User Already Exists")
        void givenAlreadyExistingUser_whenCreateUserAgain_respondUserAlreadyExisting() {
            final UserDto alreadyExistingUser = onboarding.getUserDto();
            final ExceptionResponse exceptionResponse = USERNAME_EXISTS;

            given()
                    .headers(Tokens.REQUESTER_ADMIN)
                    .contentType(ContentType.JSON)
                    .body(alreadyExistingUser)

                    .when()
                    .post(CREATE_USER_PATH)

                    .then()
                    .spec(buildResponseSpec(exceptionResponse, alreadyExistingUser.username()))
                    .statusCode(exceptionResponse.getStatusCode());
        }
    }
}
