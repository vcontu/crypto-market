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
        return exceptionResponse.getStatus().value();
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

    public static DomainException ofUserAlreadyExists(String username) {
        return new DomainException(ExceptionResponse.USERNAME_EXISTS, username);
    }

    public static DomainException ofUpdatingInactvUser(String username) {
        return new DomainException(ExceptionResponse.UPDATE_INACTV_USER, username);
    }
}