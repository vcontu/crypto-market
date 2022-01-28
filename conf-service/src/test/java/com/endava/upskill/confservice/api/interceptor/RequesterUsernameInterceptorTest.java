package com.endava.upskill.confservice.api.interceptor;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.user.Status;
import com.endava.upskill.confservice.domain.model.user.User;
import com.endava.upskill.confservice.domain.service.UserService;

import static com.endava.upskill.confservice.api.controller.UserController.REQUESTER_HEADER;
import static com.endava.upskill.confservice.util.Tokens.USERNAME_ADMIN;

@ExtendWith(MockitoExtension.class)
public class RequesterUsernameInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UserService userService;

    @Mock
    private User user;

    @InjectMocks
    private RequesterUsernameInterceptor requesterUsernameInterceptor;

    @Test
    void whenRequestWithNoUsername_thenThrowUserNotAuthorizedException() {
        assertThatThrownBy(() -> requesterUsernameInterceptor.preHandle(request, response, mock(Object.class)))
                .isEqualTo(DomainException.ofAuthenticationFailure());
    }

    @Test
    void whenRequestWithNonexistentUsername_thenThrowUserNotAuthorizedException() {
        when(request.getHeader(REQUESTER_HEADER)).thenReturn(USERNAME_ADMIN);
        when(userService.getRequester(USERNAME_ADMIN)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> requesterUsernameInterceptor.preHandle(request, response, mock(Object.class)))
                .isEqualTo(DomainException.ofAuthenticationFailure());
    }

    @Test
    void whenRequestWithUserStatusINACTV_thenThrowUserNotAuthorizedException() {
        when(request.getHeader(REQUESTER_HEADER)).thenReturn(USERNAME_ADMIN);
        when(userService.getRequester(USERNAME_ADMIN)).thenReturn(Optional.of(user));
        when(user.getStatus()).thenReturn(Status.INACTV);

        assertThatThrownBy(() -> requesterUsernameInterceptor.preHandle(request, response, mock(Object.class)))
                .isEqualTo(DomainException.ofAuthenticationFailure());
    }

    @Test
    void whenRequestWithUserADMIN_thenNoExceptionThrown() {
        when(request.getHeader(REQUESTER_HEADER)).thenReturn(USERNAME_ADMIN);
        when(userService.getRequester(USERNAME_ADMIN)).thenReturn(Optional.of(user));
        when(user.getStatus()).thenReturn(Status.ACTIVE);

        assertThatNoException().isThrownBy(() -> requesterUsernameInterceptor.preHandle(request, response, mock(Object.class)));
    }
}
