package com.endava.internship.cryptomarket.confservice.api.exceptionhandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExceptionMapperTest {

    private ObjectMapper objectMapper;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    private ExceptionMapper exceptionMapper;

    @BeforeEach
    void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        exceptionMapper = new ExceptionMapper(objectMapper);
        when(response.getWriter()).thenReturn(writer);
    }

    @ParameterizedTest
    @EnumSource(TestAsserts.class)
    void whenPrepareResponse_thenPrintInResponseExpectedValue(TestAsserts testAsserts) throws IOException {
        final int statusCode = testAsserts.errors.getApplicationError().getHttpStatus();

        exceptionMapper.prepareResponse(testAsserts.errors, response);

        verify(response).addHeader("Content-Type", "application/json; charset: UTF-8");
        verify(response).setStatus(statusCode);
        verify(response).getWriter();
        verify(writer).println(testAsserts.expectedResponse);
    }

}
