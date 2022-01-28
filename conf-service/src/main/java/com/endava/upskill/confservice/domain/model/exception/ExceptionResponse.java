package com.endava.upskill.confservice.domain.model.exception;

import lombok.Getter;

@Getter
public enum ExceptionResponse {
    AUTHENTICATION_FAILED(401,"Unauthorized", 1100, "Authentication failure. Please provide an existing username in header 'Requester-Username'."),
    AUTHORIZATION_FAILED(403, "Forbidden", 1200, "Authorization failure. Requester %s not allowed to modify users."),
    NOT_ACCEPTABLE_CONTENT(406, "Not acceptable", 2100, "The only accepted Content-Type is application/json; charset: UTF-8."),
    JSON_MALFORMED(400,"Bad request", 2200, "Request object JSON malformed."),
    REQUEST_DESERIALIZATION(400,"Bad request", 2300, "Request object validation failure."),
    USER_VALIDATION_USERNAME(400,"Bad request", 2400, "User validation failure. Username must be lowercase alphanumeric, must not begin with a number and length between 5-32."),
    USER_VALIDATION_EMAIL(400,"Bad request", 2410, "User validation failure. Email must be a valid."),
    USER_VALIDATION_STATUS(400,"Bad request", 2420, "User validation failure. The possible user status values are: ACTIVE or SUSPND."),
    USERNAME_EXISTS(400,"Bad request", 2500, "User username %s already taken."),
    USER_NOT_FOUND(404,"Not found", 3100, "User %s does not exist."),
    USER_NOT_REMOVABLE(403,"Forbidden", 4100, "User %s is not allowed to be removed."),
    INVALID_URL(404,"Not found", 9000, "Invalid resource URL."),
    INTERNAL_SERVER(500,"Internal server error", 9999, "Unknown internal error.");

    private final int statusCode;

    private final String statusText;

    private final int businessError;

    public final String messageTemplate;

    ExceptionResponse(int statusCode, String statusSuffix, int businessError, String messageTemplate) {
        this.statusCode = statusCode;
        this.statusText = statusCode + " " + statusSuffix;
        this.businessError = businessError;
        this.messageTemplate = messageTemplate;
    }

    public final String interpolateMessage(String... parameters) {
        return messageTemplate.formatted((Object[]) parameters);
    }
}

