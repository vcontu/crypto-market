package com.endava.upskill.confservice.api.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.user.Status;
import com.endava.upskill.confservice.domain.model.user.User;
import com.endava.upskill.confservice.domain.service.UserService;
import com.endava.upskill.confservice.util.Tokens;

import static com.endava.upskill.confservice.api.http.HttpHeaders.REQUESTER_USERNAME;

@ExtendWith(MockitoExtension.class)
class RequesterUsernameFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private UserService userService;

    @Mock
    private User user;

    @InjectMocks
    private RequesterUsernameFilter requesterUsernameFilter;

    @Test
    void whenRequestWithNoUsername_thenThrowUserNotAuthorizedException() {
        when(request.getHeader(REQUESTER_USERNAME)).thenReturn(null);

        assertThatThrownBy(() -> requesterUsernameFilter.doFilter(request, response, chain))
                .isEqualTo(DomainException.ofAuthenticationFailure());

        verify(request).getHeader(REQUESTER_USERNAME);
    }

    @Test
    void whenRequestWithNonexistentUsername_thenThrowUserNotAuthorizedException() {
        when(request.getHeader(REQUESTER_USERNAME)).thenReturn(Tokens.USERNAME_ADMIN);
        when(userService.getRequester(Tokens.USERNAME_ADMIN)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> requesterUsernameFilter.doFilter(request, response, chain))
                .isEqualTo(DomainException.ofAuthenticationFailure());

        verify(request).getHeader(REQUESTER_USERNAME);
        verify(userService).getRequester(Tokens.USERNAME_ADMIN);
    }

    @Test
    void whenRequestWithUserStatusINACTV_thenThrowUserNotAuthorizedException() {
        when(request.getHeader(REQUESTER_USERNAME)).thenReturn(Tokens.USERNAME_ADMIN);
        when(userService.getRequester(Tokens.USERNAME_ADMIN)).thenReturn(Optional.of(user));
        when(user.getStatus()).thenReturn(Status.INACTV);

        assertThatThrownBy(() -> requesterUsernameFilter.doFilter(request, response, chain))
                .isEqualTo(DomainException.ofAuthenticationFailure());

        verify(request).getHeader(REQUESTER_USERNAME);
        verify(userService).getRequester(Tokens.USERNAME_ADMIN);
        verify(user).getStatus();
    }

    @Test
    void whenRequestWithUserADMIN_thenNoExceptionThrown() throws ServletException, IOException {
        when(request.getHeader(REQUESTER_USERNAME)).thenReturn(Tokens.USERNAME_ADMIN);
        when(userService.getRequester(Tokens.USERNAME_ADMIN)).thenReturn(Optional.of(user));
        when(user.getStatus()).thenReturn(Status.ACTIVE);

        assertThatNoException().isThrownBy(() -> requesterUsernameFilter.doFilter(request, response, chain));

        verify(request).getHeader(REQUESTER_USERNAME);
        verify(userService).getRequester(Tokens.USERNAME_ADMIN);
        verify(user).getStatus();
        verify(chain).doFilter(request, response);
    }
}
