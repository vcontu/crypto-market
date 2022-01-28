package com.endava.upskill.confservice.util;

import lombok.Getter;

@Getter
public enum Endpoint {
    LIST_USERS("GET", "/users"),
    GET_USER("GET", "/users/{username}"),
    CREATE_USER("POST", "/users"),
    DELETE_USER("DELETE", "/users/{username}");

    private final String method;

    private final String path;

    Endpoint(final String method, final String path) {
        this.method = method;
        this.path = path;
    }
}