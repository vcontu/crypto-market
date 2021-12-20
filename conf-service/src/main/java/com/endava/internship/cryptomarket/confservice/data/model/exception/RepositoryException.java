package com.endava.internship.cryptomarket.confservice.data.model.exception;

public class RepositoryException extends RuntimeException{

    public RepositoryException(final String message, final Exception cause) {
        super(message, cause);
    }
}
