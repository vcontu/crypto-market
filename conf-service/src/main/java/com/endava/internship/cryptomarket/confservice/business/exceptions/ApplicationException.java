package com.endava.internship.cryptomarket.confservice.business.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class ApplicationException extends RuntimeException {

    private final ExceptionResponses applicationError;

    private final String messageParameter;
}
