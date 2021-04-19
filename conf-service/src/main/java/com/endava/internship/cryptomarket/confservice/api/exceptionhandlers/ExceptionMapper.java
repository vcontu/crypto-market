package com.endava.internship.cryptomarket.confservice.api.exceptionhandlers;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import com.endava.internship.cryptomarket.confservice.business.model.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ExceptionMapper {

    private final ObjectMapper objectMapper;

    public void prepareResponse(ApplicationException exception, HttpServletResponse res) {
        try {
            final ExceptionResponses serviceError = exception.getApplicationError();
            final ApiError apiError = serviceError.buildApiError(exception.getMessageParameter());
            final String response = objectMapper.writeValueAsString(apiError);

            res.addHeader("Content-Type", "application/json; charset: UTF-8");
            res.setStatus(Integer.parseInt(apiError.getStatus().substring(0, 3)));
            res.getWriter().println(response);
        } catch (IOException ignored) {
        }
    }

}
