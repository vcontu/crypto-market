package com.endava.upskill.confservice.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class CorrelationFilter extends HttpFilter {

    private static final String HEXCHARS = "0123456789abcdef";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        final String correlationId = generateCorrelationId();
        MDC.put("correlationId", correlationId);
        chain.doFilter(req, res);
        MDC.clear();
    }

    private String generateCorrelationId() {
        return RandomStringUtils.random(8, HEXCHARS) + "-" + RandomStringUtils.random(4, HEXCHARS);
    }
}
