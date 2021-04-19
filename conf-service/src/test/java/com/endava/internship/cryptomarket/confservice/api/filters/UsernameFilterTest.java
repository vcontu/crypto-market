package com.endava.internship.cryptomarket.confservice.api.filters;

import com.endava.internship.cryptomarket.confservice.business.UserService;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.data.model.Roles;
import com.endava.internship.cryptomarket.confservice.data.model.Status;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.*;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsernameFilterTest {

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
    private UsernameFilter usernameFilter;

    @Test
    void whenRequestWithNoUsername_thenThrowUserNotAuthorizedException() {
        when(request.getHeader("Requester-Username")).thenReturn(null);

        assertThatThrownBy(() -> usernameFilter.doFilter(request, response, chain))
                .isEqualTo(new ApplicationException(AUTHENTICATION_FAILURE, null));

        verify(request).getHeader("Requester-Username");
    }

    @Test
    void whenRequestWithNonexistentUsername_thenThrowUserNotAuthorizedException() {
        when(request.getHeader("Requester-Username")).thenReturn("admin");
        when(userService.getRequester("admin")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usernameFilter.doFilter(request, response, chain))
                .isEqualTo(new ApplicationException(NONEXISTENT_USER_NOT_AUTHORIZED, "admin"));

        verify(request).getHeader("Requester-Username");
        verify(userService).getRequester("admin");
    }

    @Test
    void whenRequestWithUserRoleCLIENT_thenThrowUserNotAuthorizedException() {
        when(request.getHeader("Requester-Username")).thenReturn("admin");
        when(userService.getRequester("admin")).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn(Roles.CLIENT);

        assertThatThrownBy(() -> usernameFilter.doFilter(request, response, chain))
                .isEqualTo(new ApplicationException(NONEXISTENT_USER_NOT_AUTHORIZED, "admin"));

        verify(request).getHeader("Requester-Username");
        verify(userService).getRequester("admin");
        verify(user).getRole();
    }

    @Test
    void whenRequestWithUserStatusINACTV_thenThrowUserNotAuthorizedException() {
        when(request.getHeader("Requester-Username")).thenReturn("admin");
        when(userService.getRequester("admin")).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn(Roles.OPERAT);
        when(user.getStatus()).thenReturn(Status.INACTV);

        assertThatThrownBy(() -> usernameFilter.doFilter(request, response, chain))
                .isEqualTo(new ApplicationException(NONEXISTENT_USER_NOT_AUTHORIZED, "admin"));

        verify(request).getHeader("Requester-Username");
        verify(userService).getRequester("admin");
        verify(user).getRole();
        verify(user).getStatus();
    }

    @Test
    void whenRequestWithUserStatusSUSPND_thenThrowUserAccessForbiddenException() {
        when(request.getHeader("Requester-Username")).thenReturn("admin");
        when(userService.getRequester("admin")).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn(Roles.OPERAT);
        when(user.getStatus()).thenReturn(Status.SUSPND);

        assertThatThrownBy(() -> usernameFilter.doFilter(request, response, chain))
                .isEqualTo(new ApplicationException(USER_SUSPND_ACCESS_FORBIDDEN, "admin"));

        verify(request).getHeader("Requester-Username");
        verify(userService).getRequester("admin");
        verify(user).getRole();
        verify(user, times(2)).getStatus();
    }

    @Test
    void whenRequestWithUserRoleOPERATAndMethodDelete_thenThrowUserAccessForbiddenException() {
        when(request.getHeader("Requester-Username")).thenReturn("admin");
        when(userService.getRequester("admin")).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn(Roles.OPERAT);
        when(user.getStatus()).thenReturn(Status.ACTIVE);
        when(request.getMethod()).thenReturn("DELETE");

        assertThatThrownBy(() -> usernameFilter.doFilter(request, response, chain))
                .isEqualTo(new ApplicationException(USER_NOT_ALLOWED_REMOVE, "admin"));

        verify(request).getHeader("Requester-Username");
        verify(userService).getRequester("admin");
        verify(user, times(2)).getRole();
        verify(user, times(2)).getStatus();
        verify(request).getMethod();
    }

    @Test
    void whenRequestWithUserOPERATAndNoDeleteMethod_thenNoExceptionThrown() throws ServletException, IOException {
        when(request.getHeader("Requester-Username")).thenReturn("admin");
        when(userService.getRequester("admin")).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn(Roles.OPERAT);
        when(user.getStatus()).thenReturn(Status.ACTIVE);
        when(request.getMethod()).thenReturn("PUT");

        assertThatNoException().isThrownBy(() -> usernameFilter.doFilter(request, response, chain));

        verify(request).getHeader("Requester-Username");
        verify(userService).getRequester("admin");
        verify(user, times(2)).getRole();
        verify(user, times(2)).getStatus();
        verify(request).getMethod();
        verify(chain).doFilter(request, response);
    }

    @Test
    void whenRequestWithUserADMIN_thenNoExceptionThrown() throws ServletException, IOException {
        when(request.getHeader("Requester-Username")).thenReturn("admin");
        when(userService.getRequester("admin")).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn(Roles.ADMIN);
        when(user.getStatus()).thenReturn(Status.ACTIVE);

        assertThatNoException().isThrownBy(() -> usernameFilter.doFilter(request, response, chain));

        verify(request).getHeader("Requester-Username");
        verify(userService).getRequester("admin");
        verify(user, times(2)).getRole();
        verify(user, times(2)).getStatus();
        verify(chain).doFilter(request, response);
    }

}
