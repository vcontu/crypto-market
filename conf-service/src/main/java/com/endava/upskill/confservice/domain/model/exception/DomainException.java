package com.endava.upskill.confservice.domain.model.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;


@EqualsAndHashCode(callSuper = false)
@Getter
public class DomainException extends RuntimeException {

    private final ExceptionResponse exceptionResponse;

    private DomainException(ExceptionResponse exceptionResponse, String... parameters) {
        super(exceptionResponse.interpolateMessage(parameters));
        this.exceptionResponse = exceptionResponse;
    }

    public int getStatusCode() {
        return exceptionResponse.getStatusCode();
    }

    public static DomainException ofAuthenticationFailure() {
        return new DomainException(ExceptionResponse.AUTHENTICATION_FAILED);
    }

    public static DomainException ofAuthorizationFailure(String username) {
        return new DomainException(ExceptionResponse.AUTHORIZATION_FAILED, username);
    }

    public static DomainException ofUserNotFound(String username) {
        return new DomainException(ExceptionResponse.USER_NOT_FOUND, username);
    }

    public static DomainException ofUserValidationUsername() {
        return new DomainException(ExceptionResponse.USER_VALIDATION_USERNAME);
    }

    public static DomainException ofUserAlreadyExists(String username) {
        return new DomainException(ExceptionResponse.USERNAME_EXISTS, username);
    }

    public static DomainException ofUserValidationStatus() {
        return new DomainException(ExceptionResponse.USER_VALIDATION_STATUS);
    }

    public static DomainException ofRequestObjectValidation() {
        return new DomainException(ExceptionResponse.REQUEST_DESERIALIZATION);
    }

    public static DomainException ofNotAcceptableContent() {
        return new DomainException(ExceptionResponse.NOT_ACCEPTABLE_CONTENT);
    }

    public static DomainException ofJsonMalformed() {
        return new DomainException(ExceptionResponse.JSON_MALFORMED);
    }

    public static DomainException ofInvalidUrl() {
        return new DomainException(ExceptionResponse.INVALID_URL);
    }

    public static DomainException ofInternalServerError() {
        return new DomainException(ExceptionResponse.INTERNAL_SERVER);
    }

    public static DomainException ofUserValidationEmail() {
        return new DomainException(ExceptionResponse.USER_VALIDATION_EMAIL);
    }

    public static DomainException ofUserNotRemovable(String username) {
        return new DomainException(ExceptionResponse.USER_NOT_REMOVABLE, username);
    }
}