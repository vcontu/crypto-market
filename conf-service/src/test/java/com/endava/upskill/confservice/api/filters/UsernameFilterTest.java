package com.endava.upskill.confservice.api.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernameFilterTest {

    private final UsernameFilter usernameFilter = new UsernameFilter();

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private FilterChain chainMock;

    @Mock
    private PrintWriter writerMock;

    @Test
    void whenFilterUsernameHeaderWithUnauthorisedUser_thenReturn403ForbiddenAndPrintMessage() throws IOException, ServletException {
        String expectedMessage = "Access Denied for user: userunknown";
        when(requestMock.getHeader("username")).thenReturn("userunknown");
        when(responseMock.getWriter()).thenReturn(writerMock);

        usernameFilter.doFilter(requestMock, responseMock, chainMock);

        verify(responseMock).setContentType("text/plain");
        verify(responseMock).setStatus(SC_FORBIDDEN);
        verify(writerMock).println(expectedMessage);
    }

    @Test
    void whenFilterEmptyUsernameHeader_thenReturn403ForbiddenAndPrintMessage() throws IOException, ServletException {
        String expectedMessage = "Access Denied for user: ";
        when(requestMock.getHeader("username")).thenReturn(null);
        when(responseMock.getWriter()).thenReturn(writerMock);

        usernameFilter.doFilter(requestMock, responseMock, chainMock);

        verify(responseMock).setContentType("text/plain");
        verify(responseMock).setStatus(SC_FORBIDDEN);
        verify(writerMock).println(expectedMessage);
    }

    @Test
    void whenFilterUsernameHeaderWithAuthorisedUser_thenDoFilterFurther() throws IOException, ServletException {
        when(requestMock.getHeader("username")).thenReturn("admin");

        usernameFilter.doFilter(requestMock, responseMock, chainMock);

        verify(chainMock).doFilter(requestMock, responseMock);
    }
}
