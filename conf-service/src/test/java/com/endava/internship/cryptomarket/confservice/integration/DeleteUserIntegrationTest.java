package com.endava.internship.cryptomarket.confservice.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import static com.github.springtestdbunit.annotation.DatabaseOperation.CLEAN_INSERT;
import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;

import static io.restassured.RestAssured.given;

class DeleteUserIntegrationTest extends IntegrationTest {

    private final String url = "http://localhost:8080/conf-service/users/client2";

    public DeleteUserIntegrationTest() throws Exception { }

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
    void whenDeleteUser_thenRespondAccordingToAPI() {

        given().header("Requester-Username", "admin")
                .when().delete(url)
                .then().assertThat().statusCode(204);
    }

}
