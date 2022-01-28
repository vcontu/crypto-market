package com.endava.upskill.confservice.integration;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.endava.upskill.confservice.api.http.Endpoint;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.endava.upskill.confservice.domain.model.user.Status;
import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.endava.upskill.confservice.provisioning.UserOnboarding;
import com.endava.upskill.confservice.util.ResponseValidationSpecs;
import com.endava.upskill.confservice.util.Tokens;

import static com.endava.upskill.confservice.api.http.Endpoint.*;
import static com.endava.upskill.confservice.provisioning.UserOnboarding.randomUsername;

import io.restassured.http.Headers;

import static io.restassured.RestAssured.given;

@DisplayName("Common Exceptions")
public class CommonExceptionsAT extends ResponseValidationSpecs {

    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "DELETE"})
    @DisplayName("Invalid URL Endpoint: Invalid Resource URL")
    void whenEndpointWithInvalidUrlCalled_returnApiError(String method) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.INVALID_URL;

        given()
                .headers(Tokens.REQUESTER_ADMIN)

                .when()
                .request(method, "/blabla-not-existing")

                .then()
                .spec(buildResponseSpec(exceptionResponse))
                .statusCode(exceptionResponse.getStatusCode());
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GivenWrongRequesterHeaderTest {

        @RegisterExtension
        private static final UserOnboarding SUSPENDED_USER = UserOnboarding
                .of(new UserDto(randomUsername(), "random@gmail.com", Status.SUSPND))
                .perClassOnboarding()
                .build();

        @ParameterizedTest
        @MethodSource("authenticationFailureCases")
        @DisplayName("Wrong 'Requester-Username' header: Authentication Failure")
        void givenWrongRequester_returnApiError(Headers headers, Endpoint endpoint) {
            final ExceptionResponse exceptionResponse = ExceptionResponse.AUTHENTICATION_FAILED;
            final String path = endpoint.getPath().replace("{username}", Tokens.USERNAME);

            given()
                    .headers(headers)

                    .when()
                    .request(endpoint.getMethod(), path)

                    .then()
                    .spec(buildResponseSpec(exceptionResponse))
                    .statusCode(exceptionResponse.getStatusCode());
        }

        public static Stream<Arguments> authenticationFailureCases() {
            final Headers suspendedRequester = SUSPENDED_USER.buildRequesterHeaders();
            return Stream.of(
                    Arguments.of(Tokens.REQUESTER_MISSING, CREATE_USER),
                    Arguments.of(Tokens.REQUESTER_MISSING, GET_USER),
                    Arguments.of(Tokens.REQUESTER_MISSING, LIST_USERS),
                    Arguments.of(Tokens.REQUESTER_MISSING, DELETE_USER),
                    Arguments.of(Tokens.REQUESTER_NULL, CREATE_USER),
                    Arguments.of(Tokens.REQUESTER_NULL, GET_USER),
                    Arguments.of(Tokens.REQUESTER_NULL, LIST_USERS),
                    Arguments.of(Tokens.REQUESTER_NULL, DELETE_USER),
                    Arguments.of(Tokens.REQUESTER_NOT_EXISTING, CREATE_USER),
                    Arguments.of(Tokens.REQUESTER_NOT_EXISTING, GET_USER),
                    Arguments.of(Tokens.REQUESTER_NOT_EXISTING, LIST_USERS),
                    Arguments.of(Tokens.REQUESTER_NOT_EXISTING, DELETE_USER),
                    Arguments.of(suspendedRequester, CREATE_USER),
                    Arguments.of(suspendedRequester, GET_USER),
                    Arguments.of(suspendedRequester, LIST_USERS),
                    Arguments.of(suspendedRequester, DELETE_USER)
            );
        }
    }
}
