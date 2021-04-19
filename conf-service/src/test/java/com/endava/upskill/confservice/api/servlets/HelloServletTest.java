package com.endava.upskill.confservice.api.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.service.HelloService;

@ExtendWith(MockitoExtension.class)
class HelloServletTest {

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

    @Test
    void whenSendGetRequest_thenShowMessageFromHelloService() throws IOException {
        final String TEST_MESSAGE = "Hello World!";
        when(serviceMock.getResponse()).thenReturn(TEST_MESSAGE);
        when(responseMock.getWriter()).thenReturn(writerMock);

        String testContentType = "text/plain";

        testServlet.doGet(requestMock, responseMock);

        verify(responseMock).setContentType(testContentType);
        verify(responseMock).getWriter();
        verify(writerMock).println(TEST_MESSAGE);
    }
}
