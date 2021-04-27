package com.endava.internship.cryptomarket.confservice.integration;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.Test;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static io.restassured.parsing.Parser.JSON;
import static org.hamcrest.CoreMatchers.containsString;

class PutUserIntegrationTest {

    private final String url = "http://localhost:8080/conf-service/users/operat1";

    private final UserDto newUser = new UserDto("operat1", "newOperat@gmail.com", OPERAT,
            ACTIVE, null, null, null);

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
