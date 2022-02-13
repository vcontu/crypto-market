package com.endava.upskill.confservice.api.interceptor;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.endava.upskill.confservice.api.controller.UserController;
import com.endava.upskill.confservice.domain.model.entity.Status;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class RequesterUsernameInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String username = request.getHeader(UserController.REQUESTER_HEADER);

        final boolean userAuthenticationFailure = Optional.ofNullable(username)
                .flatMap(userService::getRequester)
                .filter(user -> user.getStatus() == Status.ACTIVE)
                .isEmpty();

        if (userAuthenticationFailure) {
            log.warn("Authentication failure for requester: {}", username);
            throw DomainException.ofAuthenticationFailure();
        }
        return true;
    }
}
