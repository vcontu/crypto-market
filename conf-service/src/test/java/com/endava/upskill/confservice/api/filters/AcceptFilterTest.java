package com.endava.upskill.confservice.api.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_NOT_ACCEPTABLE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcceptFilterTest {

    private final AcceptFilter acceptFilter = new AcceptFilter();

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private FilterChain chainMock;

    @Test
    void whenFilterAcceptHeaderWithNonTextPlain_thenReturn406NonAcceptable() throws IOException, ServletException {
        int expectedStatus = SC_NOT_ACCEPTABLE;
        when(requestMock.getHeader("Accept")).thenReturn("text/html");

        acceptFilter.doFilter(requestMock, responseMock, chainMock);

        verify(responseMock).setStatus(expectedStatus);
    }

    @Test
    void whenFilterAcceptHeaderWithTextPlain_thenDoFilterFurther() throws IOException, ServletException {
        when(requestMock.getHeader("Accept")).thenReturn("text/plain");

        acceptFilter.doFilter(requestMock, responseMock, chainMock);

        verify(chainMock).doFilter(requestMock, responseMock);
    }

    @Test
    void whenFilterEmptyAcceptHeader_thenDoFilterFurther() throws IOException, ServletException {
        when(requestMock.getHeader("Accept")).thenReturn(null);

        acceptFilter.doFilter(requestMock, responseMock, chainMock);

        verify(chainMock).doFilter(requestMock, responseMock);
    }
}
