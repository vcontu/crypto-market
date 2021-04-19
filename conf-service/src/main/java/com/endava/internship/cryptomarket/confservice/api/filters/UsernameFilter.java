package com.endava.internship.cryptomarket.confservice.api.filters;

import com.endava.internship.cryptomarket.confservice.api.annotations.FilterComponent;
import com.endava.internship.cryptomarket.confservice.business.UserService;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.data.model.Roles;
import com.endava.internship.cryptomarket.confservice.data.model.Status;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Optional;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.*;
import static java.util.Objects.isNull;

@FilterComponent(path = "/*", priority = 3)
@RequiredArgsConstructor
public class UsernameFilter extends HttpFilter {

    private final UserService userService;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        final String username = req.getHeader("Requester-Username");

        if (isNull(username)) {
            throw new ApplicationException(AUTHENTICATION_FAILURE, null);
        }

        Optional<User> user = userService.getRequester(username);

        if (user.isEmpty()) {
            throw new ApplicationException(NONEXISTENT_USER_NOT_AUTHORIZED, username);
        }

        if (user.get().getRole() == Roles.CLIENT || user.get().getStatus() == Status.INACTV) {
            throw new ApplicationException(NONEXISTENT_USER_NOT_AUTHORIZED, username);
        }

        if (user.get().getStatus() == Status.SUSPND) {
            throw new ApplicationException(USER_SUSPND_ACCESS_FORBIDDEN, username);
        }
        if (user.get().getRole() == Roles.OPERAT && req.getMethod().equals("DELETE")) {
            throw new ApplicationException(USER_NOT_ALLOWED_REMOVE, username);
        }

        chain.doFilter(req, res);
    }

}
