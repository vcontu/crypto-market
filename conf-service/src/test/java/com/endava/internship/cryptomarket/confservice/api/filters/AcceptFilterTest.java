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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcceptFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private AcceptFilter acceptFilter;

    @BeforeEach
    void setUp() {
        acceptFilter = new AcceptFilter();
    }

    @Test
    void whenFilterPostRequestWithNoJsonContentType_thenThrowNotAcceptableValueException() {
        when(request.getHeader("Content-Type"))
                .thenReturn("application/random");
        when(request.getMethod()).thenReturn("POST");

        assertThatThrownBy(() -> acceptFilter.doFilter(request, response, chain))
                .isEqualTo(new ApplicationException(ExceptionResponses.NOT_ACCEPTABLE_CONTENT, null));
        verify(request).getMethod();
        verify(request).getHeader("Content-Type");
    }

    @Test
    void whenFilterPutRequestWithNoJsonContentType_thenThrowNotAcceptableValueException() {
        when(request.getHeader("Content-Type"))
                .thenReturn("application/random");
        when(request.getMethod()).thenReturn("PUT");

        assertThatThrownBy(() -> acceptFilter.doFilter(request, response, chain))
                .isEqualTo(new ApplicationException(ExceptionResponses.NOT_ACCEPTABLE_CONTENT, null));
        verify(request, times(2)).getMethod();
        verify(request).getHeader("Content-Type");
    }

    @Test
    void whenFilterGetRequestWithNoJsonContentType_thenDoFilter() throws ServletException, IOException {
        when(request.getHeader("Content-Type"))
                .thenReturn("application/random");
        when(request.getMethod()).thenReturn("GET");

        assertThatNoException().isThrownBy(() -> acceptFilter.doFilter(request, response, chain));

        verify(request, times(2)).getMethod();
        verify(request).getHeader("Content-Type");
        verify(chain).doFilter(request, response);
    }

    @Test
    void whenFilterPostRequestWithJsonContentType_thenDoFilter() throws ServletException, IOException {
        when(request.getHeader("Content-Type"))
                .thenReturn("application/json; charset: UTF-8");
        when(request.getMethod()).thenReturn("POST");

        assertThatNoException().isThrownBy(() -> acceptFilter.doFilter(request, response, chain));

        verify(request).getMethod();
        verify(request).getHeader("Content-Type");
        verify(chain).doFilter(request, response);
    }

    @Test
    void whenFilterPutRequestWithJsonContentType_thenDoFilter() throws ServletException, IOException {
        when(request.getHeader("Content-Type"))
                .thenReturn("application/json; charset: UTF-8");
        when(request.getMethod()).thenReturn("PUT");

        assertThatNoException().isThrownBy(() -> acceptFilter.doFilter(request, response, chain));

        verify(request, times(2)).getMethod();
        verify(request).getHeader("Content-Type");
        verify(chain).doFilter(request, response);
    }

}
