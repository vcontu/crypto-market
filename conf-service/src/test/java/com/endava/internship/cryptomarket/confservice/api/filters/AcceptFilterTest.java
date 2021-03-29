package com.endava.internship.cryptomarket.confservice.api.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_ACCEPTABLE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcceptFilterTest {

    private HttpFilter testFilter;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private FilterChain chainMock;

    @BeforeEach
    void setUp() {
        testFilter = new AcceptFilter();
    }

    @Test
    void whenFilterAcceptHeaderWithNonTextPlain_thenReturn406NonAcceptable() throws IOException, ServletException {
        int expectedStatus = SC_NOT_ACCEPTABLE;
        when(requestMock.getHeader("Accept")).thenReturn("text/html");

        testFilter.doFilter(requestMock, responseMock, chainMock);

        verify(responseMock).setStatus(expectedStatus);
    }

    @Test
    void whenFilterAcceptHeaderWithTextPlain_thenDoFilterFurther() throws IOException, ServletException {
        when(requestMock.getHeader("Accept")).thenReturn("text/plain");

        testFilter.doFilter(requestMock, responseMock, chainMock);

        verify(chainMock).doFilter(requestMock, responseMock);
    }

    @Test
    void whenFilterEmptyAcceptHeader_thenDoFilterFurther() throws IOException, ServletException {
        when(requestMock.getHeader("Accept")).thenReturn(null);

        testFilter.doFilter(requestMock, responseMock, chainMock);

        verify(chainMock).doFilter(requestMock, responseMock);
    }

}
