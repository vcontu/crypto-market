package com.endava.upskill.confservice.api.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.endava.upskill.confservice.api.annotation.FilterComponent;
import com.endava.upskill.confservice.api.http.HttpHeaders;
import com.endava.upskill.confservice.domain.model.user.Status;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.service.UserService;

import lombok.RequiredArgsConstructor;

@FilterComponent(path = "/*", priority = 2)
@RequiredArgsConstructor
public class RequesterUsernameFilter extends HttpFilter {

    private final UserService userService;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        final String username = req.getHeader(HttpHeaders.REQUESTER_USERNAME);

        final boolean userAuthenticationFailure = Optional.ofNullable(username)
                .flatMap(userService::getRequester)
                .filter(user -> user.getStatus() == Status.ACTIVE)
                .isEmpty();

        if (userAuthenticationFailure) {
            throw DomainException.ofAuthenticationFailure();
        }

        chain.doFilter(req, res);
    }
}
