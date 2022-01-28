package com.endava.upskill.confservice.api.filter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.endava.upskill.confservice.api.adapter.ExceptionResponseAdapter;
import com.endava.upskill.confservice.api.annotation.FilterComponent;
import com.endava.upskill.confservice.domain.model.exception.DomainException;

import lombok.RequiredArgsConstructor;

@FilterComponent(path = "/*", priority = 0)
@RequiredArgsConstructor
public class GlobalExceptionMappingFilter extends HttpFilter {

    private final ExceptionResponseAdapter exceptionResponseAdapter;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) {
        try {
            chain.doFilter(req, res);
        } catch (com.endava.upskill.confservice.domain.model.exception.DomainException e) {
            exceptionResponseAdapter.prepareResponse(e, res);
        } catch (Exception e) {
            exceptionResponseAdapter.prepareResponse(DomainException.ofInternalServerError(), res);
        }
    }
}
