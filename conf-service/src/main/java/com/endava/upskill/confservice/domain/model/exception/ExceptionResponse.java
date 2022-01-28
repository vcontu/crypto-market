package com.endava.upskill.confservice.domain.model.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionResponse {
    AUTHENTICATION_FAILED(UNAUTHORIZED, 1100, "Authentication failure. Please provide an existing username in header 'Requester-Username'."),
    AUTHORIZATION_FAILED(FORBIDDEN, 1200, "Authorization failure. Requester %s not allowed to modify users."),
    NOT_ACCEPTABLE_CONTENT(NOT_ACCEPTABLE, 2100, "The only accepted Content-Type is application/json."),
    JSON_MALFORMED(BAD_REQUEST, 2200, "Request object JSON malformed."),
    REQUEST_DESERIALIZATION(BAD_REQUEST, 2300, "Request object validation failure."),
    USER_VALIDATION_USERNAME(BAD_REQUEST, 2400,
            "User validation failure. Username must be lowercase alphanumeric, must not begin with a number and length between 5-32."),
    USER_VALIDATION_EMAIL(BAD_REQUEST, 2410, "User validation failure. Email must be a valid."),
    USER_VALIDATION_STATUS(BAD_REQUEST, 2420, "User validation failure. The possible user status values are: ACTIVE or SUSPND."),
    USERNAME_EXISTS(BAD_REQUEST, 2500, "User username %s already taken."),
    USER_NOT_FOUND(NOT_FOUND, 3100, "User %s does not exist."),
    USER_NOT_REMOVABLE(FORBIDDEN, 4100, "User %s is not allowed to be removed."),
    INVALID_URL(NOT_FOUND, 9000, "Invalid resource URL."),
    INTERNAL_SERVER(INTERNAL_SERVER_ERROR, 9999, "Unknown internal error.");

    private final HttpStatus status;

    private final int businessError;

    public final String messageTemplate;

    public int getStatusCode() {
        return status.value();
    }

    public String getStatusText() {
        return status.value() + " " + status.getReasonPhrase();
    }

    public final String interpolateMessage(String... parameters) {
        return messageTemplate.formatted((Object[]) parameters);
    }
}

