package com.endava.internship.cryptomarket.confservice.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ApiError {

    private final String status;

    private final String message;

    private final int errorCode;

    @JsonCreator
    public ApiError(@JsonProperty("status") String status, @JsonProperty("message") String message, @JsonProperty("errorCode") int errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}