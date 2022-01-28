package com.endava.upskill.confservice.api.adapter;

import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;

public record ApiExceptionResponse(String status, String message, int errorCode) {

    public ApiExceptionResponse(DomainException domainException) {
        this(domainException.getExceptionResponse(), domainException.getMessage());
    }

    public ApiExceptionResponse(ExceptionResponse exceptionResponse, String interpolatedMessage) {
        this(exceptionResponse.getStatusText(), interpolatedMessage, exceptionResponse.getBusinessError());
    }

    public static ApiExceptionResponse byInterpolating(ExceptionResponse response, String... parameters) {
        return new ApiExceptionResponse(response, response.interpolateMessage(parameters));
    }

    public static class Fields {

        public static final String status = "status";

        public static final String message = "message";

        public static final String errorCode = "errorCode";
    }
}
