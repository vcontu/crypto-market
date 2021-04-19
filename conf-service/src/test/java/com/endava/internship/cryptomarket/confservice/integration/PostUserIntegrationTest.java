package com.endava.internship.cryptomarket.confservice.integration;

import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.Test;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static io.restassured.parsing.Parser.JSON;
import static org.hamcrest.CoreMatchers.containsString;

class PostUserIntegrationTest {

    private final String url = "http://localhost:8080/conf-service/users";

    private final UserDTO newUser = new UserDTO("operat7", "newOperat@gmail.com", OPERAT,
            ACTIVE, null, null, null);

    @Test
    void whenPostUser_thenRespondAccordingToAPI() {
        final Headers postHeaders = new Headers(new Header("Content-Type", "application/json; charset: UTF-8"),
                new Header("Requester-Username", "admin"));
        defaultParser = JSON;

        given().headers(postHeaders).body(newUser)
                .when().post(url)
                .then().assertThat().statusCode(201).body(containsString("User created"));
    }

}
