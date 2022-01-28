package com.endava.upskill.confservice.api.controller;

public record ApiResponse<T>(int statusCode, T body) {

    public ApiResponse (int statusCode) {
        this(statusCode, null);
    }
}
