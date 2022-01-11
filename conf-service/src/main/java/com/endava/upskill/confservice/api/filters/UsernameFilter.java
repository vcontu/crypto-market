package com.endava.upskill.confservice.api.filters;

import java.io.IOException;
import java.io.PrintWriter;

import static java.util.Objects.nonNull;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

import com.endava.upskill.confservice.api.CustomHeaders;

@WebFilter("/restricted")
public class UsernameFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String username = req.getHeader(CustomHeaders.USERNAME);

        if ("admin".equals(username)) {
            chain.doFilter(req, res);
        } else {
            setDenyResponse(res, nonNull(username) ? username : "");
        }
    }

    private void setDenyResponse(HttpServletResponse res, String username) throws IOException {
        res.setContentType("text/plain");
        res.setStatus(SC_FORBIDDEN);

        PrintWriter writer = res.getWriter();
        writer.println("Access Denied for user: " + username);
    }
}
