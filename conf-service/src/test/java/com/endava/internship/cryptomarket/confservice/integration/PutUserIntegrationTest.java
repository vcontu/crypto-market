package com.endava.internship.cryptomarket.confservice.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static com.github.springtestdbunit.annotation.DatabaseOperation.CLEAN_INSERT;
import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;

import io.restassured.http.Header;
import io.restassured.http.Headers;

import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static io.restassured.parsing.Parser.JSON;

class PutUserIntegrationTest extends IntegrationTest {

    private final String url = "http://localhost:8080/conf-service/users/operat1";

    private final UserDto newUser = new UserDto("operat1", "newOperat@gmail.com", OPERAT,
            ACTIVE, null, null, null);

    public PutUserIntegrationTest() throws Exception { }

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
    void whenPutUser_thenRespondAccordingToAPI() {
        final Headers putHeaders = new Headers(new Header("Content-Type", "application/json"),
                new Header("Requester-Username", "admin"));
        defaultParser = JSON;

        given().headers(putHeaders).body(newUser)
                .when().put(url)
                .then().assertThat().statusCode(202).body(containsString("Accepted"));
    }

}
