package com.endava.upskill.confservice.api.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.api.controller.ApiResponse;
import com.endava.upskill.confservice.api.controller.UserController;
import com.endava.upskill.confservice.api.http.Endpoint;
import com.endava.upskill.confservice.api.http.EndpointMatcher;
import com.endava.upskill.confservice.api.http.HttpHeaders;
import com.endava.upskill.confservice.api.http.HttpMethods;
import com.endava.upskill.confservice.api.http.RequestDetails;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.user.Status;
import com.endava.upskill.confservice.domain.model.user.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.endava.upskill.confservice.util.Tokens;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.endava.upskill.confservice.api.http.Endpoint.GET_USER;
import static com.endava.upskill.confservice.api.http.HttpHeaders.APPLICATION_JSON;
import static com.endava.upskill.confservice.api.http.HttpHeaders.REQUESTER_USERNAME;
import static com.endava.upskill.confservice.api.http.HttpMethods.DELETE;
import static com.endava.upskill.confservice.api.http.HttpMethods.GET;

@ExtendWith(MockitoExtension.class)
class UsersServletTest {

    private static final String JSON = "{}";

    @Mock
    private UserController userController;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EndpointMatcher endpointMatcher;

    @InjectMocks
    private UsersServlet usersServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletInputStream inputStream;

    @Mock
    private PrintWriter writer;

    private final UserDto userDto = new UserDto(Tokens.USERNAME, "email@email.com", Status.ACTIVE);

    private final UserDetailedDto userDetailedDto = new UserDetailedDto(Tokens.USERNAME, "email@email.com", Status.ACTIVE,
            LocalDateTime.MIN, LocalDateTime.MAX, "admin");

    private void stubServletRequest(String method, String requestPath, Endpoint endpoint, String extractedRef) {
        when(request.getRequestURI()).thenReturn(requestPath);
        when(request.getMethod()).thenReturn(method);
        when(request.getHeader(REQUESTER_USERNAME)).thenReturn(Tokens.REQUESTER_USERNAME);
        when(endpointMatcher.matchEndpoint(method, requestPath)).thenReturn(new RequestDetails(endpoint, extractedRef));
    }

    private void stubServletResponse(Object objectToSerialize) throws IOException {
        when(objectMapper.writeValueAsString(objectToSerialize)).thenReturn(JSON);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void whenGetAllUsers_thenPrintServletResponse() throws IOException {
        List<UserDto> userList = List.of(userDto);
        stubServletRequest(GET, "/users/", Endpoint.LIST_USERS, null);
        stubServletResponse(userList);

        when(userController.listAllUsers()).thenReturn(new ApiResponse<>(HttpServletResponse.SC_OK, userList));

        assertThatNoException().isThrownBy(() -> usersServlet.service(request, response));

        verifyMocksForDoGet();
    }

    @Test
    void whenGetUsers_thenPrintServletResponse() throws IOException {
        final String requestPath = "/users/" + Tokens.USERNAME;
        stubServletRequest(GET, requestPath, GET_USER, Tokens.USERNAME);
        stubServletResponse(userDetailedDto);

        when(userController.getUser(Tokens.USERNAME)).thenReturn(new ApiResponse<>(HttpServletResponse.SC_OK, userDetailedDto));

        assertThatNoException().isThrownBy(() -> usersServlet.service(request, response));

        verifyMocksForDoGet();
    }

    private void verifyMocksForDoGet() {
        verify(writer).write(JSON);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void whenDelete_thenReturnStatus204() {
        String requestPath = "/users/" + Tokens.USERNAME;
        stubServletRequest(DELETE, requestPath, Endpoint.DELETE_USER, Tokens.USERNAME);
        when(userController.deleteUser(Tokens.USERNAME, Tokens.REQUESTER_USERNAME)).thenReturn(new ApiResponse<>(HttpServletResponse.SC_NO_CONTENT));

        assertThatNoException().isThrownBy(() -> usersServlet.service(request, response));

        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void whenCreateUserSuccessful_thenReturn201UserCreated() throws IOException {
        stubServletRequest(HttpMethods.POST, "/users/", Endpoint.CREATE_USER, null);

        when(request.getContentType()).thenReturn(APPLICATION_JSON);
        when(request.getInputStream()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(JSON.getBytes());
        when(objectMapper.readValue(JSON, UserDto.class)).thenReturn(userDto);
        when(request.getHeader(HttpHeaders.REQUESTER_USERNAME)).thenReturn(Tokens.USERNAME);
        when(userController.createUser(userDto, Tokens.USERNAME)).thenReturn(new ApiResponse<>(HttpServletResponse.SC_CREATED));

        assertThatNoException().isThrownBy(() -> usersServlet.service(request, response));

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @ParameterizedTest
    @MethodSource("exceptionMappings")
    void whenCreateUserObjectMapperException_thenReThrowException(Class<? extends Exception> exception,
            DomainException serviceException) throws IOException {
        stubServletRequest(HttpMethods.POST, "/users/", Endpoint.CREATE_USER, null);
        when(request.getContentType()).thenReturn(APPLICATION_JSON);

        if (exception == IOException.class) {
            doThrow(exception).when(request).getInputStream();
        } else {
            when(request.getInputStream()).thenReturn(inputStream);
            when(inputStream.readAllBytes()).thenReturn(JSON.getBytes());
            doThrow(exception).when(objectMapper).readValue(JSON, UserDto.class);
        }

        assertThatThrownBy(() -> usersServlet.service(request, response)).isEqualTo(serviceException);
    }

    private static Stream<Arguments> exceptionMappings() {
        return Stream.of(
                Arguments.of(JsonMappingException.class, DomainException.ofRequestObjectValidation()),
                Arguments.of(JsonProcessingException.class, DomainException.ofJsonMalformed()),
                Arguments.of(IOException.class, DomainException.ofInternalServerError())

        );
    }

    @Test
    void whenNoApplicationJsonHeader_thenThrowNotAcceptableContent() {
        stubServletRequest(HttpMethods.POST, "/users/", Endpoint.CREATE_USER, null);
        assertThatThrownBy(() -> usersServlet.service(request, response)).isEqualTo(DomainException.ofNotAcceptableContent());
    }
}
