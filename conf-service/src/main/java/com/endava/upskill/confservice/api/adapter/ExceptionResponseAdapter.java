package com.endava.upskill.confservice.api.adapter;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.endava.upskill.confservice.api.http.HttpHeaders;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExceptionResponseAdapter {

    private final ObjectMapper objectMapper;

    public void prepareResponse(DomainException exception, HttpServletResponse res) {
        try {
            ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(exception);
            writeServletResponse(res, apiExceptionResponse, exception.getStatusCode());
        } catch (IOException ignored) {
        }
    }

    private void writeServletResponse(HttpServletResponse res, ApiExceptionResponse apiExceptionResponse, int statusCode) throws IOException {
        res.addHeader(HttpHeaders.CONTENT_TYPE, HttpHeaders.APPLICATION_JSON);
        String response = objectMapper.writeValueAsString(apiExceptionResponse);
        res.getWriter().println(response);
        res.setStatus(statusCode);
    }
}
