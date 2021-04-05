package com.endava.internship.cryptomarket.confservice.api.servlets;

import com.endava.internship.cryptomarket.confservice.service.HelloService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HelloServletTest {

    private final String testMessage = "Hello World!";

    @InjectMocks
    private HelloServlet testServlet;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private PrintWriter writerMock;

    @Mock
    private HelloService serviceMock;

    @BeforeEach
    void setUp() throws IOException {
        when(serviceMock.getResponse()).thenReturn(testMessage);
        when(responseMock.getWriter()).thenReturn(writerMock);
    }

    @Test
    void whenSendGetRequest_thenShowMessageFromHelloService() throws IOException {
        String testContentType = "text/plain";

        testServlet.doGet(requestMock, responseMock);

        verify(responseMock).setContentType(testContentType);
        verify(responseMock).getWriter();
        verify(writerMock).println(testMessage);
    }

}
