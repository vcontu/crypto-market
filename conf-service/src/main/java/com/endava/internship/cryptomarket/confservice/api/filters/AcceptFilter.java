package com.endava.internship.cryptomarket.confservice.api.filters;

import com.endava.internship.cryptomarket.confservice.service.annotations.FilterAnnotation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_ACCEPTABLE;

@FilterAnnotation(path = "/*")
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
