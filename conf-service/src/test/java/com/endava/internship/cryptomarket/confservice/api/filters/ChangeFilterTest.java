package com.endava.internship.cryptomarket.confservice.api.filters;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangeFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private ChangeFilter changeFilter;

    @BeforeEach
    void setUp() {
        changeFilter = new ChangeFilter();
    }

    @Test
    void whenFilterPutRequestWithSameUsernameInPathAndPut_thenThrowUserAccessForbiddenException() {
        when(request.getRequestURI())
                .thenReturn("/conf-service/users/operat");
        when(request.getMethod()).thenReturn("PUT");
        when(request.getHeader("Requester-Username")).thenReturn("operat");

        assertThatThrownBy(() -> changeFilter.doFilter(request, response, chain))
                .isEqualTo(new ApplicationException(ExceptionResponses.SELF_AMEND, "operat"));
        verify(request).getMethod();
        verify(request).getHeader("Requester-Username");
        verify(request).getRequestURI();
    }

    @Test
    void whenFilterPutRequestWithDifferentUsernameInPathAndPut_thenDoFilter() throws ServletException, IOException {
        when(request.getRequestURI())
                .thenReturn("/conf-service/users/client");
        when(request.getMethod()).thenReturn("PUT");
        when(request.getHeader("Requester-Username")).thenReturn("operat");

        assertThatNoException().isThrownBy(() -> changeFilter.doFilter(request, response, chain));

        verify(request).getMethod();
        verify(request).getHeader("Requester-Username");
        verify(request).getRequestURI();
        verify(chain).doFilter(request, response);
    }

}
