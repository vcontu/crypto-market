package com.endava.internship.cryptomarket.confservice.integration;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.ADMIN;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.CLIENT;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.INACTV;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.SUSPND;
import static com.github.springtestdbunit.annotation.DatabaseOperation.CLEAN_INSERT;
import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;

import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static io.restassured.parsing.Parser.JSON;

class GetAllUsersIntegrationTest extends IntegrationTest {

    private final String url = "http://localhost:8080/conf-service/users";

    private final List<UserDto> userList = List.of(
            new UserDto("admin", "admin@gmail.com", ADMIN, ACTIVE, null, null, null),
            new UserDto("operat1", "operat1@gmail.com", OPERAT, ACTIVE, null, null, null),
            new UserDto("operat2", "operat2@gmail.com", OPERAT, ACTIVE, null, null, null),
            new UserDto("operat3", "operat3@gmail.com", OPERAT, SUSPND, null, null, null),
            new UserDto("operat4", "operat4@gmail.com", OPERAT, INACTV, null, null, null),
            new UserDto("client1", "client@gmail.com", CLIENT, ACTIVE, null, null, null)
    );

    public GetAllUsersIntegrationTest() throws Exception { }

    @BeforeEach
    void setup() throws Exception {
        super.setUp();
    }

    @AfterEach
    void teardown() throws Exception {
        super.tearDown();
    }

    @DatabaseSetup(value = "/testData.xml", type = CLEAN_INSERT)
    @DatabaseTearDown(value = "/testData.xml", type = DELETE_ALL)
    @Test
    void whenGetAllUsers_thenRespondAccordingToAPI() throws JsonProcessingException {
        defaultParser = JSON;

        UserDto[] usersDTO = given().header("Requester-Username", "admin")
                .when().get(url)
                .then().assertThat().statusCode(200)
                .extract().as(UserDto[].class);

        assertThat(usersDTO).containsAll(userList);
    }

}
