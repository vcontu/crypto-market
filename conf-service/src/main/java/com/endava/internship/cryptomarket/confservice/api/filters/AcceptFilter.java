package com.endava.internship.cryptomarket.confservice.api.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_ACCEPTABLE;

@WebFilter("/*")
public class AcceptFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Optional<String> acc = Optional.ofNullable(req.getHeader("Accept"));

        if (acc.filter(v -> v.equals("text/plain")).isPresent() || acc.isEmpty()) {
            chain.doFilter(req, res);
        }
        else{
            res.setStatus(SC_NOT_ACCEPTABLE);
        }
    }

}
