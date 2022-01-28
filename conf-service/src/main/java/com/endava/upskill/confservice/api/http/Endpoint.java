package com.endava.upskill.confservice.api.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.endava.upskill.confservice.api.http.HttpMethods.DELETE;
import static com.endava.upskill.confservice.api.http.HttpMethods.GET;
import static com.endava.upskill.confservice.api.http.HttpMethods.POST;

import lombok.Getter;

public enum Endpoint {
    LIST_USERS(GET, false, "/users", "/conf-service/users/?"),
    GET_USER(GET, true, "/users/{username}", "/conf-service/users/([a-z0-9]{5,32})/?"),
    CREATE_USER(POST, false, "/users", "/conf-service/users/?"),
    DELETE_USER(DELETE, true, "/users/{username}", "/conf-service/users/([a-z0-9]{5,32})/?");

    @Getter
    private final String method;

    @Getter
    private final String path;

    private final Pattern pattern;

    @Getter
    private final boolean extractable;

    Endpoint(final String method, boolean extractable, final String path, String regex) {
        this.method = method;
        this.path = path;
        this.pattern = Pattern.compile(regex);
        this.extractable = extractable;
    }

    public Matcher getMatcher(String charSequence) {
        return pattern.matcher(charSequence);
    }
}