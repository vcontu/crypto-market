package com.endava.upskill.confservice.api.adapter;

import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public record ApiExceptionResponse(String status, String message, int errorCode) {

    public ApiExceptionResponse(ExceptionResponse exceptionResponse, String interpolatedMessage) {
        this(exceptionResponse.getStatusText(), interpolatedMessage, exceptionResponse.getBusinessError());
    }

    public static ApiExceptionResponse byInterpolating(ExceptionResponse response, String... parameters) {
        return new ApiExceptionResponse(response, response.interpolateMessage(parameters));
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static class Fields {

        public static final String status = "status";

        public static final String message = "message";

        public static final String errorCode = "errorCode";
    }
}
