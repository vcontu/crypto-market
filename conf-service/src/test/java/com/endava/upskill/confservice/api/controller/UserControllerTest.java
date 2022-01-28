package com.endava.upskill.confservice.api.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.api.adapter.ExceptionResponseAdapter;
import com.endava.upskill.confservice.domain.model.user.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.endava.upskill.confservice.domain.service.UserService;
import com.endava.upskill.confservice.provisioning.UserOnboarding;
import com.endava.upskill.confservice.util.Endpoint;
import com.endava.upskill.confservice.util.ResponseValidationSpecs;
import com.endava.upskill.confservice.util.Tokens;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@ExtendWith(MockitoExtension.class)
class UserControllerTest extends ResponseValidationSpecs {

    private UserController userController;

    @Mock
    private UserService userService;

    /**
     * Spring Test MockMvc is perfectly viable to test the controllers. However it cannot test real instance of REST API. For that case you need RestAssured. In order to decrease
     * the cognitive load of multiple REST API test libraries it was decided to use RestAssured adapter for MockMvc. Thus all the REST testing will be done with RestAssured's DSL.
     */
    @BeforeEach
    void setUp() {
        userController = new UserController(userService, Tokens.CLOCK_FIXED);
        final ExceptionResponseAdapter exceptionResponseAdapter = new ExceptionResponseAdapter();

        //the default ObjectMapper will write dates as timestamps. This feature must be disabled.
        final MappingJackson2HttpMessageConverter jackson = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = jackson.getObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setMessageConverters(jackson)
                .build();

        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void listUsers() {
        final List<UserDto> userDtos = List.of(UserOnboarding.randomUser(), UserOnboarding.randomUser());
        when(userService.getAllUsers()).thenReturn(userDtos);

        given()
                .headers(Tokens.REQUESTER_ADMIN)

                .when()
                .get(Endpoint.LIST_USERS.getPath())

                .then()
                .log().body()
                .spec(buildResponseSpec(userDtos))
                .status(HttpStatus.OK);
    }

    @Test
    void getUser() {
        final UserDetailedDto userDetailed = UserOnboarding.randomUserDetailed();
        when(userService.getUser(userDetailed.username())).thenReturn(userDetailed);

        given()
                .headers(Tokens.REQUESTER_ADMIN)

                .when()
                .get(Endpoint.GET_USER.getPath(), userDetailed.username())

                .then()
                .log().body()
                .spec(buildResponseSpec(userDetailed))
                .status(HttpStatus.OK);
    }

    @Test
    void createUser() {
        final UserDto userDto = UserOnboarding.randomUser();

        given()
                .headers(Tokens.REQUESTER_ADMIN)
                .contentType(ContentType.JSON)
                .body(userDto)

                .when()
                .post(Endpoint.CREATE_USER.getPath())

                .then()
                .log().body()
                .status(HttpStatus.CREATED);

        verify(userService).createUser(userDto, Tokens.LDT, Tokens.USERNAME_ADMIN);
    }

    @Test
    void deleteUser() {
        given()
                .headers(Tokens.REQUESTER_ADMIN)

                .when()
                .delete(Endpoint.DELETE_USER.getPath(), Tokens.USERNAME)

                .then()
                .status(HttpStatus.NO_CONTENT);

        verify(userService).deleteUser(Tokens.USERNAME, Tokens.USERNAME_ADMIN);
    }
}