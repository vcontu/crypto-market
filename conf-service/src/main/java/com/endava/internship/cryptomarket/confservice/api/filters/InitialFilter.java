package com.endava.internship.cryptomarket.confservice.api.filters;

import com.endava.internship.cryptomarket.confservice.api.annotations.FilterComponent;
import com.endava.internship.cryptomarket.confservice.api.exceptionhandlers.ExceptionMapper;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.INTERNAL_SERVER_ERROR;

@FilterComponent(path = "/*", priority = 0)
@RequiredArgsConstructor
public class InitialFilter extends HttpFilter {

    private final ExceptionMapper exceptionMapper;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } catch (ApplicationException e) {
            exceptionMapper.prepareResponse(e, res);
        } catch (Exception e) {
            exceptionMapper.prepareResponse(new ApplicationException(INTERNAL_SERVER_ERROR, null), res);
        }
    }
}
