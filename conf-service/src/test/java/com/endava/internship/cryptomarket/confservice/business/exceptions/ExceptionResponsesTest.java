package com.endava.internship.cryptomarket.confservice.business.exceptions;

import com.endava.internship.cryptomarket.confservice.business.model.ApiError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionResponsesTest {

    private ExceptionResponses exceptionResponses;

    private String username = "testUsername";

    @BeforeEach
    private void setUp() {
        exceptionResponses = ExceptionResponses.USER_NOT_FOUND;
    }

    @Test
    void whenBuildApiError_thenReturnBuildedApiError() {
        ApiError expectedApiError = new ApiError("404 Not found", "User " + username + " does not exist.", 4100);

        ApiError apiError = exceptionResponses.buildApiError(username);

        assertThat(expectedApiError).isEqualTo(apiError);
    }

}
