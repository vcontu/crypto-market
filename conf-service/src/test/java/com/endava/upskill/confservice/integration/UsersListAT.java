package com.endava.upskill.confservice.integration;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.endava.upskill.confservice.provisioning.UserOnboarding;
import com.endava.upskill.confservice.util.Endpoint;
import com.endava.upskill.confservice.util.ResponseValidationSpecs;
import com.endava.upskill.confservice.util.Tokens;

import static io.restassured.RestAssured.given;

@DisplayName("GET /users")
public class UsersListAT extends ResponseValidationSpecs {

    @RegisterExtension
    private final UserOnboarding onboarding1 = new UserOnboarding();

    @RegisterExtension
    private final UserOnboarding onboarding2 = new UserOnboarding();

    @Test
    @DisplayName("List users will display all created users")
    void whenMultipleUsersAreCreated_respondWithAll() {
        final List<UserDto> userDtoList = List.of(onboarding1.getUserDto(), onboarding2.getUserDto());

        given()
                .headers(Tokens.REQUESTER_ADMIN)

                .when()
                .get(Endpoint.LIST_USERS.getPath())

                .then()
                .spec(buildResponseSpec(userDtoList))
                .statusCode(HttpServletResponse.SC_OK);
    }
}
