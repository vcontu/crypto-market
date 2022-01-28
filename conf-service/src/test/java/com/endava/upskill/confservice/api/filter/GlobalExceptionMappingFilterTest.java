package com.endava.upskill.confservice.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.endava.upskill.confservice.api.adapter.ExceptionResponseAdapter;
import com.endava.upskill.confservice.domain.model.exception.DomainException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionMappingFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private ExceptionResponseAdapter exceptionResponseAdapter;

    @InjectMocks
    private GlobalExceptionMappingFilter globalExceptionMappingFilter;

    @Test
    void whenDoFilterThrowsServiceException_thenPrepareResponse() throws ServletException, IOException {
        doThrow(DomainException.ofInternalServerError())
                .when(chain).doFilter(request, response);

        globalExceptionMappingFilter.doFilter(request, response, chain);

        verify(exceptionResponseAdapter)
                .prepareResponse(DomainException.ofInternalServerError(), response);
    }

    @Test
    void whenDoFilterThrowsException_thenPrepareResponse() throws ServletException, IOException {
        doThrow(new RuntimeException())
                .when(chain).doFilter(request, response);

        globalExceptionMappingFilter.doFilter(request, response, chain);

        verify(exceptionResponseAdapter)
                .prepareResponse(DomainException.ofInternalServerError(), response);
    }

    @Test
    void whenDoFilterNoExceptionThrown_thenResponseNotPrepared() throws ServletException, IOException {
        globalExceptionMappingFilter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verifyNoInteractions(exceptionResponseAdapter);
    }
}
