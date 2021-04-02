package com.endava.internship.cryptomarket.confservice.api.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_ACCEPTABLE;

@WebFilter("/*")
public class AcceptFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String accept = req.getHeader("Accept");

        if (accept == null || accept.equals("text/plain")) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(SC_NOT_ACCEPTABLE);
        }
    }

}
