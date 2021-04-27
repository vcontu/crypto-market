package com.endava.internship.cryptomarket.confservice.integration;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.*;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.*;
import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static io.restassured.parsing.Parser.JSON;
import static org.assertj.core.api.Assertions.assertThat;

class GetAllUsersIntegrationTest {

    private final String url = "http://localhost:8080/conf-service/users";

    private final List<UserDto> userList = List.of(
            new UserDto("admin", "admin@gmail.com", ADMIN, ACTIVE, null, null, null),
            new UserDto("operat1", "operat1@gmail.com", OPERAT, ACTIVE, null, null, null),
            new UserDto("operat2", "operat2@gmail.com", OPERAT, ACTIVE, null, null, null),
            new UserDto("operat3", "operat3@gmail.com", OPERAT, SUSPND, null, null, null),
            new UserDto("operat4", "operat4@gmail.com", OPERAT, INACTV, null, null, null),
            new UserDto("client1", "client@gmail.com", CLIENT, ACTIVE, null, null, null)
    );

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
