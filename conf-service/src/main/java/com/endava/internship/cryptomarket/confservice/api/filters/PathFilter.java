package com.endava.internship.cryptomarket.confservice.api.filters;

import com.endava.internship.cryptomarket.confservice.api.annotations.FilterComponent;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@FilterComponent(path = "/users/*", priority = 2)
public class PathFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String[] path = req.getRequestURI().split("/");
        if (req.getMethod().equals("PUT") && path.length != 4
                || req.getMethod().equals("DELETE") && path.length != 4
                || req.getMethod().equals("POST") && path.length != 3
                || req.getMethod().equals("GET") && path.length > 4) {
            throw new ApplicationException(ExceptionResponses.NONEXISTENT_ENDPOINT, null);
        }

        chain.doFilter(req, res);
    }
}
