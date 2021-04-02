package com.endava.internship.cryptomarket.confservice.api.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestrictedServletTest {

    private RestrictedServlet testServlet;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private PrintWriter writerMock;

    @BeforeEach
    void setUp() throws IOException {
        testServlet = new RestrictedServlet();

        when(responseMock.getWriter()).thenReturn(writerMock);
        when(requestMock.getHeader("username")).thenReturn("admin");
    }

    @Test
    void whenSendGetRequest_thenGrantAccessForUser() throws IOException {
        String testContentType = "text/plain";
        String testMessage = "Access Granted for user: admin";

        testServlet.doGet(requestMock, responseMock);

        verify(responseMock).setContentType(testContentType);
        verify(responseMock).getWriter();
        verify(writerMock).println(testMessage);
    }

}
