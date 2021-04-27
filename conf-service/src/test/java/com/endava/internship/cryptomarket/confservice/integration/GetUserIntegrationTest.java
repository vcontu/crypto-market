package com.endava.internship.cryptomarket.confservice.integration;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import org.junit.jupiter.api.Test;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static io.restassured.parsing.Parser.JSON;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class GetUserIntegrationTest {

    private final String url = "http://localhost:8080/conf-service/users/operat1";

    private final UserDto expectedUser = new UserDto("operat1", "operat1@gmail.com", OPERAT,
            ACTIVE, now(), null, null);


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
