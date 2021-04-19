package com.endava.internship.cryptomarket.confservice.integration;

import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.*;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.*;
import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static io.restassured.parsing.Parser.JSON;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class GetAllUsersIntegrationTest {

    private final String url = "http://localhost:8080/conf-service/users";

    private final List<UserDTO> userList = List.of(
            new UserDTO("admin", "admin@gmail.com", ADMIN, ACTIVE, null, null, null),
            new UserDTO("operat1", "operat1@gmail.com", OPERAT, ACTIVE, null, null, null),
            new UserDTO("operat2", "operat2@gmail.com", OPERAT, ACTIVE, null, null, null),
            new UserDTO("operat3", "operat3@gmail.com", OPERAT, SUSPND, null, null, null),
            new UserDTO("operat4", "operat4@gmail.com", OPERAT, INACTV, null, null, null),
            new UserDTO("client1", "client@gmail.com", CLIENT, ACTIVE, null, null, null)
    );

    @Test
    void whenGetAllUsers_thenRespondAccordingToAPI() throws JsonProcessingException {
        defaultParser = JSON;

        UserDTO[] usersDTO = given().header("Requester-Username", "admin")
                .when().get(url)
                .then().assertThat().statusCode(200).header("Accept", "application/json; charset: UTF-8")
                .extract().as(UserDTO[].class);

        assertThat(usersDTO).containsAll(userList);
    }

}
