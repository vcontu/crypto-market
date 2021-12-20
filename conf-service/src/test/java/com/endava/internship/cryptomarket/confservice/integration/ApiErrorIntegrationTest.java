package com.endava.internship.cryptomarket.confservice.integration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

import com.endava.internship.cryptomarket.confservice.business.model.ApiError;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import static com.github.springtestdbunit.annotation.DatabaseOperation.CLEAN_INSERT;
import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;

import io.restassured.http.Header;
import io.restassured.http.Headers;

import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static io.restassured.parsing.Parser.JSON;

class ApiErrorIntegrationTest extends IntegrationTest {

    public ApiErrorIntegrationTest() throws Exception {
        super.setUp();
    }

    @DatabaseSetup(value = "/testData.xml", type = CLEAN_INSERT)
    @DatabaseTearDown(value = "/testData.xml", type = DELETE_ALL)
    @ParameterizedTest
    @EnumSource(value = ApiErrorTest.class)
    void whenRequestInvalid_thenRespondAccordingToAPI(ApiErrorTest test) {
        final Headers postHeaders = new Headers(new Header("Content-Type", test.getContentType()),
                new Header("Requester-Username", test.getRequesterUsername()));
        defaultParser = JSON;

        ApiError apiError = given().headers(postHeaders).body(test.getMessageParam())
                .when().request(test.getMethod(), test.getUrlPath())
                .then().assertThat().statusCode(test.getExceptionResponses().getHttpStatus())
                .extract().as(ApiError.class);

        assertThat(apiError).isEqualTo(test.getExceptionResponses().buildApiError(test.getExceptionParam()));

    }

}
