package com.endava.upskill.confservice.api.filters;

import java.io.IOException;

import static java.util.Objects.isNull;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_NOT_ACCEPTABLE;

@WebFilter("/*")
public class AcceptFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String acceptHeader = req.getHeader("Accept");

        if (isNull(acceptHeader) || acceptHeader.equals("text/plain")) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(SC_NOT_ACCEPTABLE);
        }
    }
}
