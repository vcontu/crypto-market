package com.endava.internship.cryptomarket.confservice.integration;

import com.endava.internship.cryptomarket.confservice.business.model.ApiError;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static io.restassured.parsing.Parser.JSON;
import static org.assertj.core.api.Assertions.assertThat;

class ApiErrorIntegrationTest {

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
