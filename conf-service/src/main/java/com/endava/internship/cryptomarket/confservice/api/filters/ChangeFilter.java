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

@FilterComponent(path = "/*", priority = 4)
public class ChangeFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        final String[] path = req.getRequestURI().split("/");
        String usernameToChange = "";
        if (path.length == 4) {
            usernameToChange = path[3];
        }
        if (req.getMethod().equals("PUT")
                && req.getHeader("Requester-Username").equals(usernameToChange)) {
            throw new ApplicationException(ExceptionResponses.SELF_AMEND, usernameToChange);
        }

        chain.doFilter(req, res);
    }
}
