package com.endava.upskill.confservice.integration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.endava.upskill.confservice.domain.model.create.UserDto;
import com.endava.upskill.confservice.domain.model.entity.Status;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.endava.upskill.confservice.domain.model.get.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.update.UserUpdateDto;
import com.endava.upskill.confservice.provisioning.UserOnboarding;
import com.endava.upskill.confservice.util.Endpoint;
import com.endava.upskill.confservice.util.ResponseValidationSpecs;
import com.endava.upskill.confservice.util.Tokens;

import static com.endava.upskill.confservice.domain.model.exception.ExceptionResponse.*;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.mapper.ObjectMapperType;
import lombok.RequiredArgsConstructor;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("PATCH /users")
public class UsersUpdateAT extends ResponseValidationSpecs {

    private static final String UPDATE_USER_PATH = Endpoint.UPDATE_USER.getPath();

    @Test
    @DisplayName("Wrong Content-Type header: Not Acceptable")
    void givenWrongContentType_respondNotAcceptable() {
        final ExceptionResponse exceptionResponse = NOT_ACCEPTABLE_CONTENT;

        given()
                .headers(Tokens.REQUESTER_ADMIN)
                .contentType("text/plain")
                .body(UserOnboarding.randomUser(), ObjectMapperType.JACKSON_2)

                .when()
                .patch(UPDATE_USER_PATH, Tokens.USERNAME)

                .then()
                .spec(buildResponseSpec(exceptionResponse))
                .statusCode(exceptionResponse.getStatusCode());
    }

    @Nested
    class GivenOnboardedUserTest {

        private static final String USERNAME_2 = "username2";

        @RegisterExtension
        private static final UserOnboarding onboarding = UserOnboarding.of(new UserDto(Tokens.USERNAME, Tokens.EMAIL, Status.ACTIVE))
                .perClassOnboarding().build();

        @ParameterizedTest
        @EnumSource(UserUpdates.class)
        @DisplayName("Request object with invalid parameters: Validation Failures")
        void givenCreateUserWithInvalidRequestObject_respondWithException(UserUpdates userUpdate) {
            given()
                    .headers(userUpdate.headers)
                    .contentType(ContentType.JSON)
                    .body(userUpdate.requestObject)

                    .when()
                    .patch(UPDATE_USER_PATH, userUpdate.username)

                    .then()
                    .spec(buildResponseSpec(userUpdate.exceptionResponse, userUpdate.username))
                    .statusCode(userUpdate.exceptionResponse.getStatusCode());
        }

        @RequiredArgsConstructor
        private enum UserUpdates {
            json_malformed(JSON_MALFORMED, "Corrupted JSON", Tokens.USERNAME),
            request_deserialization(REQUEST_DESERIALIZATION, """
                    {
                        "username": "%s",
                        "email":    "username06@gmail.com",
                        "status":   "NOT-EXISTING"
                    }""".formatted(Tokens.USERNAME), Tokens.USERNAME),
            user_not_updatable(USER_NOT_UPDATABLE, new UserUpdateDto(Tokens.USERNAME_ADMIN, Tokens.EMAIL, null), Tokens.USERNAME_ADMIN),
            username_different(USERNAME_DIFFERENT, new UserUpdateDto(USERNAME_2, Tokens.EMAIL, null), Tokens.USERNAME),
            username_different2(USERNAME_DIFFERENT, new UserUpdateDto(null, Tokens.EMAIL, null), Tokens.USERNAME),
            user_update_no_properties(USER_UPDATE_NO_PROPERTIES, new UserUpdateDto(Tokens.USERNAME, null, null), Tokens.USERNAME),
            user_validation_email(USER_VALIDATION_EMAIL, new UserUpdateDto(Tokens.USERNAME, "inv__3!@$%^&&*mail", null), Tokens.USERNAME),
            user_not_found(USER_NOT_FOUND, new UserUpdateDto(Tokens.USERNAME_NOT_EXISTING, Tokens.EMAIL, null), Tokens.USERNAME_NOT_EXISTING);

            private final ExceptionResponse exceptionResponse;

            private final Headers headers;

            private final Object requestObject;

            private final String username;

            UserUpdates(ExceptionResponse exceptionResponse, Object requestObject, String username) {
                this.exceptionResponse = exceptionResponse;
                this.headers = Tokens.REQUESTER_ADMIN;
                this.requestObject = requestObject;
                this.username = username;
            }
        }
    }

    @Nested
    class GivenInactvUserTest {

        @RegisterExtension
        private static final UserOnboarding onboarding = UserOnboarding.ofRandomUser().perClassOnboarding().build();

        private final String username = onboarding.getUsername();

        @Test
        void whenUpdateInactvUser_thenThrowUpdateInactvUser() {
            given()
                    .headers(Tokens.REQUESTER_ADMIN)
                    .spec(buildRequestSpec(new UserUpdateDto(username, null, Status.INACTV)))

                    .when()
                    .patch(UPDATE_USER_PATH, username)

                    .then()
                    .statusCode(HttpStatus.ACCEPTED.value());

            final ExceptionResponse exceptionResponse = UPDATE_INACTV_USER;

            given()
                    .headers(Tokens.REQUESTER_ADMIN)
                    .spec(buildRequestSpec(new UserUpdateDto(username, null, Status.ACTIVE)))

                    .when()
                    .patch(UPDATE_USER_PATH, username)

                    .then()
                    .spec(buildResponseSpec(exceptionResponse, username))
                    .statusCode(exceptionResponse.getStatusCode());
        }
    }

    @Nested
    class GivenUserOnboardedTest {

        @RegisterExtension
        private final UserOnboarding requesterOnboarding = new UserOnboarding();

        @RegisterExtension
        private final UserOnboarding userOnboarding = new UserOnboarding();

        private final String username = userOnboarding.getUsername();

        @Test
        void whenGetUserDetails_thenUpdatedByAndUpdateOnIsActualized() {
            given()
                    .header(new Header(Tokens.REQUESTER_HEADER, requesterOnboarding.getUsername()))
                    .spec(buildRequestSpec(new UserUpdateDto(username, null, Status.SUSPND)))

                    .when()
                    .patch(UPDATE_USER_PATH, username)

                    .then()
                    .statusCode(SC_ACCEPTED);
            final LocalDateTime userUpdatedTime = LocalDateTime.now(Tokens.CLOCK_CONF_SERVICE);

            final UserDetailedDto userDetailedDto =
                    given()
                            .headers(Tokens.REQUESTER_ADMIN)

                            .when()
                            .get(Endpoint.GET_USER.getPath(), username)

                            .then()
                            .spec(buildResponseSpec(new UserDto(username, userOnboarding.getUserDto().email(), Status.SUSPND)))
                            .statusCode(SC_OK)
                            .extract().as(UserDetailedDto.class);

            //We do not know what is the time difference between test engine and test instance. Let's assume that 5 min is the max;
            assertThat(userDetailedDto.createdOn()).isCloseTo(userUpdatedTime, within(5, ChronoUnit.MINUTES));
            assertThat(userDetailedDto.updatedOn()).isCloseTo(userUpdatedTime, within(5, ChronoUnit.MINUTES));
            assertThat(userDetailedDto.updatedBy()).isEqualTo(requesterOnboarding.getUsername());
        }
    }
}
