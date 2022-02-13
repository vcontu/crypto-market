package com.endava.upskill.confservice.integration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static javax.servlet.http.HttpServletResponse.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.Matchers.hasItem;

import com.endava.upskill.confservice.domain.model.create.UserDto;
import com.endava.upskill.confservice.domain.model.entity.Status;
import com.endava.upskill.confservice.domain.model.get.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.update.UserUpdateDto;
import com.endava.upskill.confservice.provisioning.UserOnboarding;
import com.endava.upskill.confservice.util.Endpoint;
import com.endava.upskill.confservice.util.ResponseValidationSpecs;
import com.endava.upskill.confservice.util.Tokens;

import static com.endava.upskill.confservice.util.Endpoint.CREATE_USER;
import static com.endava.upskill.confservice.util.Endpoint.UPDATE_USER;

import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsersGeneralFlowAT extends ResponseValidationSpecs {

    private final UserDto userDto = UserOnboarding.randomUser();

    private final String username = userDto.username();

    private LocalDateTime userCreatedTime;

    @Test
    @Order(1)
    @DisplayName("Create User: Success")
    void createUser_successful() {
        given()
                .headers(Tokens.REQUESTER_ADMIN)
                .spec(buildRequestSpec(userDto))

                .when()
                .post(CREATE_USER.getPath())

                .then()
                .statusCode(SC_CREATED);

        userCreatedTime = LocalDateTime.now(Tokens.CLOCK_CONF_SERVICE);
    }

    @Test
    @Order(2)
    @DisplayName("Get User: Success")
    void getUser_successful() {
        final UserDetailedDto userDetailedDto =
                given()
                        .headers(Tokens.REQUESTER_ADMIN)

                        .when()
                        .get(Endpoint.GET_USER.getPath(), username)

                        .then()
                        .spec(buildResponseSpec(userDto))
                        .statusCode(SC_OK)
                        .extract().as(UserDetailedDto.class);

        //We do not know what is the time difference between test engine and test instance. Let's assume that 5 min is the max;
        assertThat(userDetailedDto.createdOn()).isCloseTo(userCreatedTime, within(5, ChronoUnit.MINUTES));
        assertThat(userDetailedDto.updatedOn()).isCloseTo(userCreatedTime, within(5, ChronoUnit.MINUTES));
        assertThat(userDetailedDto.updatedBy()).isEqualTo(Tokens.USERNAME_ADMIN);
    }

    @Test
    @Order(3)
    @DisplayName("List Users: Success")
    void listAllUsers_successful() {
        final JsonPath jsonPath =
                given()
                        .headers(Tokens.REQUESTER_ADMIN)

                        .when()
                        .get(Endpoint.LIST_USERS.getPath())

                        .then()
                        .body("findAll { it.username == '%s' }.username".formatted(username), hasItem(username))
                        .statusCode(SC_OK)
                        .extract().jsonPath();

        final UserDto extractedUserDto = jsonPath.getObject("find { it.username == '%s' }".formatted(username), UserDto.class);
        assertThat(extractedUserDto).isEqualTo(userDto);
    }

    @Test
    @Order(4)
    @DisplayName("Update User: Success")
    void updateUser_accepted() {
        given()
                .headers(Tokens.REQUESTER_ADMIN)
                .spec(buildRequestSpec(new UserUpdateDto(username, null, Status.INACTV)))

                .when()
                .patch(UPDATE_USER.getPath(), username)

                .then()
                .statusCode(SC_ACCEPTED);
    }

    @Test
    @Order(5)
    @DisplayName("Delete User: Success")
    void deleteUser_successful() {
        given()
                .headers(Tokens.REQUESTER_ADMIN)

                .when()
                .delete(Endpoint.DELETE_USER.getPath(), username)

                .then()
                .statusCode(SC_NO_CONTENT);
    }
}
