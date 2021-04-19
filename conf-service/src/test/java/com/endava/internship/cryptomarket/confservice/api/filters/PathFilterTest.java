package com.endava.internship.cryptomarket.confservice.api.filters;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private PathFilter pathFilter;

    private static Stream<Arguments> provideStringsForIncorrectPath() {
        return Stream.of(
                Arguments.of("PUT", "/conf-service/users"),
                Arguments.of("DELETE", "/conf-service/users"),
                Arguments.of("POST", "/conf-service/users/user"),
                Arguments.of("GET", "/conf-service/users/user/find")
        );
    }

    private static Stream<Arguments> provideStringsForCorrectPath() {
        return Stream.of(
                Arguments.of("PUT", "/conf-service/users/user"),
                Arguments.of("DELETE", "/conf-service/users/user"),
                Arguments.of("POST", "/conf-service/users"),
                Arguments.of("GET", "/conf-service/users/user")
        );
    }

    @BeforeEach
    void setUp() {
        pathFilter = new PathFilter();
    }

    @ParameterizedTest
    @MethodSource("provideStringsForIncorrectPath")
    void whenRequestPutWithIncorrectPath_thenThrowNonexistentEndpointException(String method, String URI) {
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(URI);

        assertThatThrownBy(() -> pathFilter.doFilter(request, response, chain))
                .isEqualTo(new ApplicationException(ExceptionResponses.NONEXISTENT_ENDPOINT, null));
    }

    @ParameterizedTest
    @MethodSource("provideStringsForCorrectPath")
    void whenRequestPutWithCorrectPath_thenNoExceptionThrown(String method, String URI) throws ServletException, IOException {
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(URI);

        assertThatNoException().isThrownBy(() -> pathFilter.doFilter(request, response, chain));

        verify(chain).doFilter(request, response);
    }

}
