package com.endava.upskill.confservice.api.filter;

import java.io.IOException;
import java.util.EnumSet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.endava.upskill.confservice.api.annotation.FilterComponent;
import com.endava.upskill.confservice.api.http.Endpoint;
import com.endava.upskill.confservice.domain.model.exception.DomainException;

@FilterComponent(path = "/*", priority = 1)
public class EndpointFilter extends HttpFilter {

    @Override
    protected void doFilter(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain) throws IOException, ServletException {
        final boolean endpointMethodExists = EnumSet.allOf(Endpoint.class).stream()
                .anyMatch(endpoint -> endpoint.getMethod().equals(req.getMethod()));

        if (!endpointMethodExists) {
            throw DomainException.ofInvalidUrl();
        }
        chain.doFilter(req, res);
    }
}
