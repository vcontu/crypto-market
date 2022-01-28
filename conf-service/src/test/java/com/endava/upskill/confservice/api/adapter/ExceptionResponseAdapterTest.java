package com.endava.upskill.confservice.api.adapter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.*;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.api.http.HttpHeaders;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@ExtendWith(MockitoExtension.class)
class ExceptionResponseAdapterTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ExceptionResponseAdapter exceptionResponseAdapter = new ExceptionResponseAdapter(objectMapper);

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    @Captor
    private ArgumentCaptor<String> jsonCaptor;

    @BeforeEach
    void setUp() throws IOException {
        when(response.getWriter()).thenReturn(writer);
    }

    @ParameterizedTest
    @EnumSource(TestCases.class)
    void whenPrepareResponse_thenPrintInResponseExpectedValue(TestCases testCases) throws IOException, JSONException {
        exceptionResponseAdapter.prepareResponse(testCases.getDomainException(), response);

        verify(response).addHeader(HttpHeaders.CONTENT_TYPE, HttpHeaders.APPLICATION_JSON);
        verify(response).setStatus(testCases.getStatusCode());
        verify(response).getWriter();
        verify(writer).println(jsonCaptor.capture());

        String actualResult = jsonCaptor.getValue();
        JSONAssert.assertEquals(testCases.getExpectedResponse(), actualResult, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Getter
    private enum TestCases {
        TEST_InvalidResourceUrl(DomainException.ofInvalidUrl(), SC_NOT_FOUND, """
                {
                    "status":"404 Not found",
                    "message":"Invalid resource URL.",
                    "errorCode":9000
                }"""),
        TEST_AuthenticationFailure(DomainException.ofAuthenticationFailure(), SC_UNAUTHORIZED, """
                {
                    "status":"401 Unauthorized",
                    "message":"Authentication failure. Please provide an existing username in header 'Requester-Username'.",
                    "errorCode":1100
                }"""),
        TEST_AuthorizationFailure(DomainException.ofAuthorizationFailure("user"), SC_FORBIDDEN, """
                {
                    "status":"403 Forbidden",
                    "message":"Authorization failure. Requester user not allowed to modify users.",
                    "errorCode":1200
                }"""),
        TEST_NotAcceptableContent(DomainException.ofNotAcceptableContent(), SC_NOT_ACCEPTABLE, """
                {
                    "status":"406 Not acceptable",
                    "message":"The only accepted Content-Type is application/json; charset: UTF-8.",
                    "errorCode":2100
                }"""),
        TEST_JsonMalformed(DomainException.ofJsonMalformed(), SC_BAD_REQUEST, """
                {
                    "status":"400 Bad request",
                    "message":"Request object JSON malformed.",
                    "errorCode":2200
                }"""),
        TEST_RequestObjectValidationFailure(DomainException.ofRequestObjectValidation(), SC_BAD_REQUEST, """
                {
                    "status":"400 Bad request",
                    "message":"Request object validation failure.",
                    "errorCode":2300
                }"""),
        TEST_UserValidationFailureUsername(DomainException.ofUserValidationUsername(), SC_BAD_REQUEST, """
                {
                    "status":"400 Bad request",
                    "message":"User validation failure. Username must be lowercase alphanumeric, must not begin with a number and length between 5-32.",
                    "errorCode":2400
                }"""),
        TEST_UserValidationFailureEmail(DomainException.ofUserValidationEmail(), SC_BAD_REQUEST, """
                {
                    "status":"400 Bad request",
                    "message":"User validation failure. Email must be a valid.",
                    "errorCode":2410
                }"""),
        TEST_UserValidationFailureStatus(DomainException.ofUserValidationStatus(), SC_BAD_REQUEST, """
                {
                    "status":"400 Bad request",
                    "message":"User validation failure. The possible user status values are: ACTIVE or SUSPND.",
                    "errorCode":2420
                }"""),
        TEST_UserAlreadyExists(DomainException.ofUserAlreadyExists("user"), SC_BAD_REQUEST, """
                {
                    "status":"400 Bad request",
                    "message":"User username user already taken.",
                    "errorCode":2500
                }"""),
        TEST_UserNotFound(DomainException.ofUserNotFound("user"), SC_NOT_FOUND, """
                {
                    "status":"404 Not found",
                    "message":"User user does not exist.",
                    "errorCode":3100
                }"""),
        TEST_DeleteUserForbidden(DomainException.ofUserNotRemovable("admin"), SC_FORBIDDEN, """
                {
                    "status":"403 Forbidden",
                    "message":"User admin is not allowed to be removed.",
                    "errorCode":4100
                }"""),
        TEST_InternalServerError(DomainException.ofInternalServerError(), SC_INTERNAL_SERVER_ERROR, """
                {
                    "status":"500 Internal server error",
                    "message":"Unknown internal error.",
                    "errorCode":9999
                }""");

        private final DomainException domainException;

        private final int statusCode;

        private final String expectedResponse;

        TestCases(DomainException exception, int statusCode, String jsonResponse) {
            this.domainException = exception;
            this.statusCode = statusCode;
            this.expectedResponse = jsonResponse;
        }
    }
}
