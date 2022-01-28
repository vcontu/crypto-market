package com.endava.upskill.confservice.api.adapter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.fasterxml.jackson.core.JsonParseException;

import static com.endava.upskill.confservice.domain.model.exception.ExceptionResponse.*;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionResponseAdapter extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Object> handleApplicationException(DomainException exception) {
        log.warn("Domain Exception: {}", exception.getExceptionResponse());
        return mapToResponseEntity(exception.getExceptionResponse(), exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolations(ConstraintViolationException ex) {
        final ConstraintViolation<?> firstViolation = ex.getConstraintViolations().iterator().next();
        final ExceptionResponse exceptionResponse = (ExceptionResponse) firstViolation.getConstraintDescriptor().getAttributes().get("exceptionResponse");
        log.warn("Constraint Exception: {}", exceptionResponse);
        return mapToResponseEntity(exceptionResponse, firstViolation.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex.getCause() instanceof JsonParseException) {
            log.warn("Api Exception: {}", JSON_MALFORMED);
            return mapToResponseEntity(JSON_MALFORMED);
        }
        log.warn("Api Exception: {}", REQUEST_DESERIALIZATION);
        return mapToResponseEntity(REQUEST_DESERIALIZATION);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("Api Exception: {}", NOT_ACCEPTABLE_CONTENT);
        return mapToResponseEntity(NOT_ACCEPTABLE_CONTENT);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("Api Exception: {}", INVALID_URL);
        return mapToResponseEntity(INVALID_URL);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Global Exception: {}", INTERNAL_SERVER);
        return mapToResponseEntity(INTERNAL_SERVER);
    }

    private static ResponseEntity<Object> mapToResponseEntity(ExceptionResponse exceptionResponse, String interpolatedMessage) {
        return new ResponseEntity<>(new ApiExceptionResponse(exceptionResponse, interpolatedMessage), exceptionResponse.getStatus());
    }

    private static ResponseEntity<Object> mapToResponseEntity(ExceptionResponse exceptionResponse) {
        return new ResponseEntity<>(new ApiExceptionResponse(exceptionResponse, exceptionResponse.interpolateMessage()), exceptionResponse.getStatus());
    }
}