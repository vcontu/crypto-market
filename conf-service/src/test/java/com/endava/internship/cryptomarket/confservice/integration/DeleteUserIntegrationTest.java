package com.endava.internship.cryptomarket.confservice.integration;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

class DeleteUserIntegrationTest {

    private final String url = "http://localhost:8080/conf-service/users/client2";

    @Test
    void whenDeleteUser_thenRespondAccordingToAPI() {

        given().header("Requester-Username", "admin")
                .when().delete(url)
                .then().assertThat().statusCode(204);
    }

}
