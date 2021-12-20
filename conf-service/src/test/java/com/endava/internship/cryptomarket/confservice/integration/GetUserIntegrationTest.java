package com.endava.internship.cryptomarket.confservice.integration;

import static java.time.LocalDateTime.now;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static com.github.springtestdbunit.annotation.DatabaseOperation.CLEAN_INSERT;
import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;

import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static io.restassured.parsing.Parser.JSON;

class GetUserIntegrationTest extends IntegrationTest {

    private final String url = "http://localhost:8080/conf-service/users/operat1";

    private final UserDto expectedUser = new UserDto("operat1", "operat1@gmail.com", OPERAT,
            ACTIVE, now(), null, null);

    public GetUserIntegrationTest() throws Exception { }

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
    void whenGetUser_thenRespondAccordingToAPI() {
        defaultParser = JSON;

        UserDto user = given().header("Requester-Username", "admin")
                .when().get(url)
                .then().assertThat().statusCode(200)
                .extract().as(UserDto.class);

        assertThat(user).isEqualTo(expectedUser);
    }
}
