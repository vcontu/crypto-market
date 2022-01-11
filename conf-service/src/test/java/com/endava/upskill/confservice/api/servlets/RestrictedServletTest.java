package com.endava.upskill.confservice.api.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.api.CustomHeaders;

@ExtendWith(MockitoExtension.class)
class RestrictedServletTest {

    private final RestrictedServlet testServlet = new RestrictedServlet();

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private PrintWriter writerMock;

    @Test
    void whenSendGetRequest_thenGrantAccessForUser() throws IOException {
        when(responseMock.getWriter()).thenReturn(writerMock);
        when(requestMock.getHeader(CustomHeaders.USERNAME)).thenReturn("admin");

        testServlet.doGet(requestMock, responseMock);

        verify(responseMock).setContentType("text/plain");
        verify(responseMock).getWriter();
        verify(writerMock).println("Access Granted for user: admin");
    }
}
