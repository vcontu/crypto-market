package com.endava.internship.cryptomarket.confservice.api.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

@WebFilter("/restricted")
public class UsernameFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Optional<String> username = Optional.ofNullable(req.getHeader("username"));

        if(username.filter(v -> v.equals("admin")).isEmpty()){
            setDenyResponse(res, username.orElse(""));
        }
        else {
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
