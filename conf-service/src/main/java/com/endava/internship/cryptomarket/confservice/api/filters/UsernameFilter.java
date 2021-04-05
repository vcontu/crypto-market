package com.endava.internship.cryptomarket.confservice.api.filters;

import com.endava.internship.cryptomarket.confservice.service.annotations.FilterAnnotation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

@FilterAnnotation(path = "/restricted")
public class UsernameFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        final String username = req.getHeader("username");

        if (!"admin".equals(username)) {
            setDenyResponse(res, username == null ? "" : username);
        } else {
            chain.doFilter(req, res);
        }
    }

    private void setDenyResponse(HttpServletResponse res, String username) throws IOException {
        res.setContentType("text/plain");
        res.setStatus(SC_FORBIDDEN);

        PrintWriter writer = res.getWriter();
        writer.println("Access Denied for user: " + username);
    }

}
