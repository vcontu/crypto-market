package com.endava.internship.cryptomarket.confservice.api.exceptionhandlers;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.*;

public enum TestAsserts {

    TEST1(new ApplicationException(NONEXISTENT_USER_NOT_AUTHORIZED, "user"),
            "{" +
                    "\"status\":\"401 Unauthorized\"," +
                    "\"message\":\"Requester user does not exist.\"," +
                    "\"errorCode\":1000" +
                    "}"),
    TEST2(new ApplicationException(AUTHENTICATION_FAILURE, "user"),
            "{" +
                    "\"status\":\"401 Unauthorized\"," +
                    "\"message\":\"Authentication failure. Please provide header 'Requester-Username'.\"," +
                    "\"errorCode\":1000" +
                    "}"),
    TEST3(new ApplicationException(NONEXISTENT_ENDPOINT, "user"),
            "{" +
                    "\"status\":\"404 Not found\"," +
                    "\"message\":\"Endpoint dose not exist.\"," +
                    "\"errorCode\":1201" +
                    "}"),
    TEST4(new ApplicationException(USER_SUSPND_ACCESS_FORBIDDEN, "user"),
            "{" +
                    "\"status\":\"403 Forbidden\"," +
                    "\"message\":\"Requester user is currently suspended. Please contact the administrator.\"," +
                    "\"errorCode\":2100" +
                    "}"),
    TEST5(new ApplicationException(INACTIV_USER_AMEND, "user"),
            "{" +
                    "\"status\":\"400 Bad request\"," +
                    "\"message\":\"User user is inactive and cannot be amended.\"," +
                    "\"errorCode\":2200" +
                    "}"),
    TEST6(new ApplicationException(INACTIV_USER_CREATE, "user"),
            "{" +
                    "\"status\":\"400 Bad request\"," +
                    "\"message\":\"Unable to create user user with initial status INACTV.\"," +
                    "\"errorCode\":2300" +
                    "}"),
    TEST7(new ApplicationException(USER_NOT_ALLOWED_REMOVE, "user"),
            "{" +
                    "\"status\":\"403 Forbidden\"," +
                    "\"message\":\"Requester user is not allowed to remove users.\"," +
                    "\"errorCode\":3100" +
                    "}"),
    TEST8(new ApplicationException(ADMIN_NOT_ALLOWED_CHANGE_ADMIN, "user"),
            "{" +
                    "\"status\":\"403 Forbidden\"," +
                    "\"message\":\"Requester user is not allowed to create other administrators.\"," +
                    "\"errorCode\":3200" +
                    "}"),
    TEST9(new ApplicationException(OPERAT_NOT_ALLOWED_CHANGE_ADMIN_OPERAT, "user"),
            "{" +
                    "\"status\":\"403 Forbidden\"," +
                    "\"message\":\"Requester user is not allowed to create other operators or administrators.\"," +
                    "\"errorCode\":3300" +
                    "}"),
    TEST10(new ApplicationException(USER_NOT_FOUND, "user"),
            "{" +
                    "\"status\":\"404 Not found\"," +
                    "\"message\":\"User user does not exist.\"," +
                    "\"errorCode\":4100" +
                    "}"),
    TEST11(new ApplicationException(SELF_AMEND, "user"),
            "{" +
                    "\"status\":\"403 Forbidden\"," +
                    "\"message\":\"Requester user is not allowed to amend itself.\"," +
                    "\"errorCode\":4200" +
                    "}"),
    TEST12(new ApplicationException(DIFFERENT_USERNAME, "user"),
            "{" +
                    "\"status\":\"400 Bad request\"," +
                    "\"message\":\"Username must be the same URL path variable and request object property username.\"," +
                    "\"errorCode\":4300" +
                    "}"),
    TEST13(new ApplicationException(NOT_ACCEPTABLE_CONTENT, "user"),
            "{" +
                    "\"status\":\"406 Not acceptable\"," +
                    "\"message\":\"For endpoints with request objects, request Header Content-Type is not application/json or null\"," +
                    "\"errorCode\":5000" +
                    "}"),
    TEST14(new ApplicationException(JSON_MALFORMED, "user"),
            "{" +
                    "\"status\":\"400 Bad request\"," +
                    "\"message\":\"Request object JSON malformed.\"," +
                    "\"errorCode\":5100" +
                    "}"),
    TEST15(new ApplicationException(USER_NOT_CHANGED, "user"),
            "{" +
                    "\"status\":\"400 Bad request\"," +
                    "\"message\":\"Request object requires at least one property to be amended. Username cannot be amended.\"," +
                    "\"errorCode\":5200" +
                    "}"),
    TEST16(new ApplicationException(USER_MISSING_PROPERTY, "user"),
            "{" +
                    "\"status\":\"400 Bad request\"," +
                    "\"message\":\"Request object missing required properties.\"," +
                    "\"errorCode\":5300" +
                    "}"),
    TEST17(new ApplicationException(USER_ILLEGAL_STATE, "user"),
            "{" +
                    "\"status\":\"400 Bad request\"," +
                    "\"message\":\"Request object validation failure.\"," +
                    "\"errorCode\":5400" +
                    "}"),
    TEST18(new ApplicationException(REQUEST_OBJECT_VALIDATION_FAILURE, "user"),
            "{" +
                    "\"status\":\"400 Bad request\"," +
                    "\"message\":\"Request object validation failure.\"," +
                    "\"errorCode\":5400" +
                    "}"),
    TEST19(new ApplicationException(USER_ALREADY_EXISTS, "user"),
            "{" +
                    "\"status\":\"400 Bad request\"," +
                    "\"message\":\"User username user already taken.\"," +
                    "\"errorCode\":5400" +
                    "}"),
    TEST20(new ApplicationException(INTERNAL_SERVER_ERROR, "user"),
            "{" +
                    "\"status\":\"500 Internal server error\"," +
                    "\"message\":\"Unknown internal error\"," +
                    "\"errorCode\":9999" +
                    "}");

    ApplicationException errors;
    String expectedResponse;

    TestAsserts(ApplicationException errors, String expectedResponse) {
        this.errors = errors;
        this.expectedResponse = expectedResponse;
    }

}
