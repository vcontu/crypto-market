package com.endava.internship.cryptomarket.confservice.api.filters;

import com.endava.internship.cryptomarket.confservice.api.exceptionhandlers.ExceptionMapper;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.INTERNAL_SERVER_ERROR;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InitialFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private ExceptionMapper exceptionMapper;

    @InjectMocks
    private InitialFilter initialFilter;

    @Test
    void whenDoFilterThrowsServiceException_thenPrepareResponse() throws ServletException, IOException {
        doThrow(new ApplicationException(INTERNAL_SERVER_ERROR, null)).when(chain).doFilter(request, response);

        initialFilter.doFilter(request, response, chain);

        verify(exceptionMapper).prepareResponse(new ApplicationException(INTERNAL_SERVER_ERROR, null), response);
    }

    @Test
    void whenDoFilterThrowsException_thenPrepareResponse() throws ServletException, IOException {
        doThrow(new RuntimeException()).when(chain).doFilter(request, response);

        initialFilter.doFilter(request, response, chain);

        verify(exceptionMapper).prepareResponse(new ApplicationException(INTERNAL_SERVER_ERROR, null), response);
    }

    @Test
    void whenDoFilterNoExceptionThrown_thenResponseNotPrepared() throws ServletException, IOException {
        initialFilter.doFilter(request, response, chain);

        verify(exceptionMapper, never()).prepareResponse(new ApplicationException(INTERNAL_SERVER_ERROR, null), response);
    }
}
