package com.endava.upskill.confservice.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.domain.model.exception.DomainException;

@ExtendWith(MockitoExtension.class)
class EndpointFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private final EndpointFilter endpointFilter = new EndpointFilter();

    @ParameterizedTest
    @ValueSource(strings = {"GET","POST", "DELETE"})
    void whenRequestPutWithCorrectPath_thenNoExceptionThrown(String method) throws ServletException, IOException {
        when(request.getMethod()).thenReturn(method);

        assertThatNoException().isThrownBy(() -> endpointFilter.doFilter(request, response, chain));

        verify(chain).doFilter(request, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"PUT", "PATCH", "OPTIONS"})
    void whenRequestPutWithIncorrectPath_thenThrowNonexistentEndpointException(String method) {
        when(request.getMethod()).thenReturn(method);

        assertThatThrownBy(() -> endpointFilter.doFilter(request, response, chain))
                .isEqualTo(DomainException.ofInvalidUrl());
    }
}
