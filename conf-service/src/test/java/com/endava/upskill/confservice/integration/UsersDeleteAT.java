package com.endava.upskill.confservice.integration;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.endava.upskill.confservice.provisioning.UserOnboarding;
import com.endava.upskill.confservice.util.Endpoint;
import com.endava.upskill.confservice.util.ResponseValidationSpecs;
import com.endava.upskill.confservice.util.Tokens;

import static io.restassured.RestAssured.given;

@DisplayName("DELETE /users/{username}")
public class UsersDeleteAT extends ResponseValidationSpecs {

    private static final String DELETE_USER_PATH = Endpoint.DELETE_USER.getPath();

    @Test
    @DisplayName("Delete non existing user: User does not exist")
    void whenDeletingNonExitingUser_respondUserDoesNotExist() {
        final ExceptionResponse exceptionResponse = ExceptionResponse.USER_NOT_FOUND;

        given()
                .headers(Tokens.REQUESTER_ADMIN)

                .when()
                .delete(DELETE_USER_PATH, Tokens.USERNAME_NOT_EXISTING)

                .then()
                .spec(buildResponseSpec(exceptionResponse, Tokens.USERNAME_NOT_EXISTING))
                .statusCode(exceptionResponse.getStatusCode());
    }

    @Test
    @DisplayName("Deleting admin user: Delete Forbidden")
    void whenDeletingAdminUser_respondCannotDeleteAdmin() {
        final ExceptionResponse exceptionResponse = ExceptionResponse.USER_NOT_REMOVABLE;

        given()
                .headers(Tokens.REQUESTER_ADMIN)

                .when()
                .delete(DELETE_USER_PATH, Tokens.USERNAME_ADMIN)

                .then()
                .spec(buildResponseSpec(exceptionResponse, Tokens.USERNAME_ADMIN))
                .statusCode(exceptionResponse.getStatusCode());
    }

    @Nested
    @DisplayName("Create user with Requester non-admin: Authorization Failure")
    class GivenOnboardedUserTest {

        @RegisterExtension
        private final UserOnboarding requester = new UserOnboarding();

        @RegisterExtension
        private final UserOnboarding existingUser = new UserOnboarding();

        @Test
        @DisplayName("Delete non existing user: User does not exist")
        void whenDeletingNonExitingUser_respondUserDoesNotExist() {
            final ExceptionResponse exceptionResponse = ExceptionResponse.AUTHORIZATION_FAILED;

            given()
                    .headers(requester.buildRequesterHeaders())

                    .when()
                    .delete(DELETE_USER_PATH, existingUser.getUsername())

                    .then()
                    .spec(buildResponseSpec(exceptionResponse, requester.getUsername()))
                    .statusCode(exceptionResponse.getStatusCode());
        }
    }

    @Nested
    class GivenExistingUserAboutToBeDeletedTest {

        @RegisterExtension
        private final UserOnboarding onboarding = UserOnboarding.ofRandomUser().onboardingOnly().build();

        @Test
        @DisplayName("Deleted user no longer exists in the database")
        void givenUserIsDeleted_whenGetUser_respondUserDoesNotExist() {
            final String username = onboarding.getUsername();
            given()
                    .headers(Tokens.REQUESTER_ADMIN)

                    .when()
                    .delete(DELETE_USER_PATH, username)

                    .then()
                    .statusCode(HttpServletResponse.SC_NO_CONTENT);

            final ExceptionResponse exceptionResponse = ExceptionResponse.USER_NOT_FOUND;

            given()
                    .headers(Tokens.REQUESTER_ADMIN)

                    .when()
                    .get(Endpoint.GET_USER.getPath(), username)

                    .then()
                    .spec(buildResponseSpec(exceptionResponse, username))
                    .statusCode(exceptionResponse.getStatusCode());
        }
    }
}
