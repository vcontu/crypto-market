package com.endava.internship.cryptomarket.confservice.api.filters;

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
import java.io.PrintWriter;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernameFilterTest {

    private UsernameFilter testFilter;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private FilterChain chainMock;

    @Mock
    private PrintWriter writerMock;

    @BeforeEach
    void setUp() {
        testFilter = new UsernameFilter();
    }

    @Test
    void whenFilterUsernameHeaderWithUnauthorisedUser_thenReturn403ForbiddenAndPrintMessage() throws IOException, ServletException {
        String expectedMessage = "Access Denied for user: userunknown";
        when(requestMock.getHeader("username")).thenReturn("userunknown");
        when(responseMock.getWriter()).thenReturn(writerMock);

        testFilter.doFilter(requestMock, responseMock, chainMock);

        verify(responseMock).setContentType("text/plain");
        verify(responseMock).setStatus(SC_FORBIDDEN);
        verify(writerMock).println(expectedMessage);
    }

    @Test
    void whenFilterEmptyUsernameHeader_thenReturn403ForbiddenAndPrintMessage() throws IOException, ServletException {
        String expectedMessage = "Access Denied for user: ";
        when(requestMock.getHeader("username")).thenReturn(null);
        when(responseMock.getWriter()).thenReturn(writerMock);

        testFilter.doFilter(requestMock, responseMock, chainMock);

        verify(responseMock).setContentType("text/plain");
        verify(responseMock).setStatus(SC_FORBIDDEN);
        verify(writerMock).println(expectedMessage);
    }

    @Test
    void whenFilterUsernameHeaderWithAuthorisedUser_thenDoFilterFurther() throws IOException, ServletException {
        when(requestMock.getHeader("username")).thenReturn("admin");

        testFilter.doFilter(requestMock, responseMock, chainMock);

        verify(chainMock).doFilter(requestMock, responseMock);
    }

}
