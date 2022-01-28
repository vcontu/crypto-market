package com.endava.upskill.confservice.api.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.fasterxml.jackson.core.JsonParseException;

import static com.endava.upskill.confservice.util.Tokens.USERNAME;

@ExtendWith(MockitoExtension.class)
class ExceptionResponseAdapterTest {

    private final static HttpStatus DUMMY_HTTP_STATUS = HttpStatus.BAD_REQUEST;

    private final ExceptionResponseAdapter exceptionAdapter = new ExceptionResponseAdapter();

    @Mock
    private HttpHeaders headers;

    @Mock
    private WebRequest request;

    @Test
    void handleDomainException() {
        final DomainException exception = DomainException.ofUserAlreadyExists(USERNAME);

        final ResponseEntity<Object> responseEntity = exceptionAdapter.handleApplicationException(exception);

        final ApiExceptionResponse expectedResponseObject = ApiExceptionResponse.byInterpolating(exception.getExceptionResponse(), USERNAME);
        assertEquals(expectedResponseObject, responseEntity.getBody());
        assertEquals(exception.getStatusCode(), responseEntity.getStatusCodeValue());
    }

    @Test
    void handleHttpMessageNotReadable_forAllPurposeErrors() {
        final var exception = mock(HttpMessageNotReadableException.class);
        final ResponseEntity<Object> responseEntity = exceptionAdapter.handleHttpMessageNotReadable(exception, headers, DUMMY_HTTP_STATUS, request);

        final ExceptionResponse response = ExceptionResponse.REQUEST_DESERIALIZATION;
        assertEquals(ApiExceptionResponse.byInterpolating(response), responseEntity.getBody());
        assertEquals(response.getStatusCode(), responseEntity.getStatusCodeValue());
        verifyNoInteractions(headers, request);
    }

    @Test
    void handleHttpMessageNotReadable_forJsonMalformed() {
        final var exception = mock(HttpMessageNotReadableException.class);
        when(exception.getCause()).thenReturn(mock(JsonParseException.class));
        final ResponseEntity<Object> responseEntity = exceptionAdapter.handleHttpMessageNotReadable(exception, headers, DUMMY_HTTP_STATUS, request);

        final ExceptionResponse response = ExceptionResponse.JSON_MALFORMED;
        assertEquals(ApiExceptionResponse.byInterpolating(response), responseEntity.getBody());
        assertEquals(response.getStatusCode(), responseEntity.getStatusCodeValue());
        verifyNoInteractions(headers, request);
    }

    @Test
    void handleHttpMediaTypeNotSupported() {
        final var exception = mock(HttpMediaTypeNotSupportedException.class);
        final ResponseEntity<Object> responseEntity = exceptionAdapter.handleHttpMediaTypeNotSupported(exception, headers, DUMMY_HTTP_STATUS, request);

        final ExceptionResponse response = ExceptionResponse.NOT_ACCEPTABLE_CONTENT;
        assertEquals(ApiExceptionResponse.byInterpolating(response), responseEntity.getBody());
        assertEquals(response.getStatusCode(), responseEntity.getStatusCodeValue());
        verifyNoInteractions(exception, headers, request);
    }

    @Test
    void handleNoHandlerFoundException() {
        final var exception = mock(NoHandlerFoundException.class);
        final ResponseEntity<Object> responseEntity = exceptionAdapter.handleNoHandlerFoundException(exception, headers, DUMMY_HTTP_STATUS, request);

        final ExceptionResponse response = ExceptionResponse.INVALID_URL;
        assertEquals(ApiExceptionResponse.byInterpolating(response), responseEntity.getBody());
        assertEquals(response.getStatusCode(), responseEntity.getStatusCodeValue());
        verifyNoInteractions(exception, headers, request);
    }

    @Test
    void handleExceptionInternal() {
        final var exception = mock(BindException.class);
        final var object = mock(Object.class);
        final ResponseEntity<Object> responseEntity = exceptionAdapter.handleExceptionInternal(exception, object, headers, DUMMY_HTTP_STATUS, request);

        final ExceptionResponse response = ExceptionResponse.INTERNAL_SERVER;
        assertEquals(ApiExceptionResponse.byInterpolating(response), responseEntity.getBody());
        assertEquals(response.getStatusCode(), responseEntity.getStatusCodeValue());
        verifyNoInteractions(exception, headers, request);
    }
}
