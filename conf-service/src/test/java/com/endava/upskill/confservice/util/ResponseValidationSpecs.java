package com.endava.upskill.confservice.util;

import java.util.List;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import com.endava.upskill.confservice.api.adapter.ApiExceptionResponse;
import com.endava.upskill.confservice.api.http.HttpHeaders;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.endava.upskill.confservice.domain.model.user.UserDto;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public abstract class ResponseValidationSpecs {

    static {
        RestAssured.baseURI = "http://localhost/conf-service";
        RestAssured.port = 8080;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public static RequestSpecification buildRequestSpec(UserDto userDto) {
        return new RequestSpecBuilder()
                .setContentType(HttpHeaders.APPLICATION_JSON)
                .setBody(userDto)
                .build();
    }

    public static ResponseSpecification buildResponseSpec(UserDto expected) {
        return new ResponseSpecBuilder()
                .expectContentType(HttpHeaders.APPLICATION_JSON)
                .expectBody(UserDto.Fields.username, equalTo(expected.username()))
                .expectBody(UserDto.Fields.email, equalTo(expected.email()))
                .expectBody(UserDto.Fields.status, equalTo (expected.status().name()))
                .build();
    }

    public static ResponseSpecification buildResponseSpec(List<UserDto> list) {
        Function<Function<UserDto, String>, String[]> fromList =
                dtoFunction -> list.stream().map(dtoFunction).toArray(String[]::new);

        return new ResponseSpecBuilder()
                .expectContentType(HttpHeaders.APPLICATION_JSON)
                .expectBody(UserDto.Fields.username, hasItems(fromList.apply(UserDto::username)))
                .expectBody(UserDto.Fields.email, hasItems(fromList.apply(UserDto::email)))
                .expectBody(UserDto.Fields.status, hasItems(fromList.apply(userDto -> userDto.status().name())))
                .build();
    }

    public static ResponseSpecification buildResponseSpec(ExceptionResponse exceptionResponse, String... parameters) {
        ApiExceptionResponse apiExceptionResponse = ApiExceptionResponse.byInterpolating(exceptionResponse, parameters);

        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectBody(ApiExceptionResponse.Fields.status, equalTo(apiExceptionResponse.status()))
                .expectBody(ApiExceptionResponse.Fields.message, equalTo(apiExceptionResponse.message()))
                .expectBody(ApiExceptionResponse.Fields.errorCode, equalTo(apiExceptionResponse.errorCode()))
                .build();
    }
}
