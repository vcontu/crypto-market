package com.endava.internship.cryptomarket.confservice.api;

import com.endava.internship.cryptomarket.confservice.api.servlets.UsersServlet;
import com.endava.internship.cryptomarket.confservice.business.UserService;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
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
import java.util.ArrayList;
import java.util.List;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.CLIENT;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServletTest {

    private final String requestUsername = "admin";
    private final String usernameToChange = "user";
    @Mock
    private UserService userService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletInputStream inputStream;
    @Mock
    private PrintWriter writer;
    @InjectMocks
    private UsersServlet usersServlet;

    private UserDTO user;

    private final String jsonArray = "[{\"name\": \"user\",\"email\": \"email@email.com\",\"role\": \"OPERAT\",\"status\": \"ACTIVE\"}]";

    private final String json = "{\"name\": \"user\",\"email\": \"email@email.com\",\"role\": \"OPERAT\",\"status\": \"ACTIVE\"}";


    @BeforeEach
    void setUp() {
        user = new UserDTO(requestUsername, "email@email.com", OPERAT, ACTIVE,
                null, null, null);
    }

    @Test
    void whenDelete_thenReturnStatus204() {
        String username = "user";
        when(request.getRequestURI()).thenReturn("/conf-service/users/" + username);

        usersServlet.doDelete(request, response);

        verify(userService).deleteUser(username);
        verify(response).setStatus(204);
    }

    @Test
    void whenPutUrlUsernameAndPropertyDifferent_thenThrowDifferentUsernameException() throws IOException {
        final UserDTO userDTO = new UserDTO("user", "operat1@gmail.com", CLIENT, ACTIVE,
                null, null, null);
        when(request.getRequestURI()).thenReturn("/conf-service/users/otherUser");
        when(request.getHeader("Requester-Username")).thenReturn("user");
        when(request.getInputStream()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(json.getBytes());
        when(objectMapper.readValue(json, UserDTO.class)).thenReturn(userDTO);

        assertThatThrownBy(() -> usersServlet.doPut(request, response)).isEqualTo(new ApplicationException(ExceptionResponses.DIFFERENT_USERNAME, null));

        verify(request).getRequestURI();
        verify(request).getHeader("Requester-Username");
        verify(request).getInputStream();
        verify(inputStream).readAllBytes();
        verify(objectMapper).readValue(json, UserDTO.class);
    }

    @Test
    void whenPut_thenReturn202Accepted() throws IOException {
        final UserDTO userDTO = new UserDTO("user", "operat1@gmail.com", CLIENT, ACTIVE,
                null, null, null);
        when(request.getRequestURI()).thenReturn("/conf-service/users/" + usernameToChange);
        when(request.getHeader("Requester-Username")).thenReturn(requestUsername);
        when(request.getInputStream()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(json.getBytes());
        when(objectMapper.readValue(json, UserDTO.class)).thenReturn(userDTO);
        when(response.getWriter()).thenReturn(writer);

        assertThatNoException().isThrownBy(() -> usersServlet.doPut(request, response));

        verify(request).getRequestURI();
        verify(request).getHeader("Requester-Username");
        verify(request).getInputStream();
        verify(inputStream).readAllBytes();
        verify(objectMapper).readValue(json, UserDTO.class);
        verify(userService).amendUser(eq(usernameToChange), eq(userDTO), eq(requestUsername), any());
        verify(response).setStatus(202);
        verify(writer).println("Accepted");
    }

    @Test
    void whenGetUser_thenPrepareUserResponse() throws IOException {
        String username = "user";
        when(request.getRequestURI()).thenReturn("/conf-service/users/" + username);
        when(userService.getUser(username)).thenReturn(user);
        when(objectMapper.writeValueAsString(user)).thenReturn(json);
        when(response.getWriter()).thenReturn(writer);

        usersServlet.doGet(request, response);

        verify(request).getRequestURI();
        verify(userService).getUser(username);
        verify(objectMapper).writeValueAsString(user);
        verify(writer).write(json);
        verify(response).addHeader("Accept", "application/json; charset: UTF-8");
        verify(response).setStatus(200);
    }

    @Test
    void whenGetAllUsers_thenPrepareUsersResponse() throws IOException {
        when(request.getRequestURI()).thenReturn("/conf-service/users/");
        List<UserDTO> users = List.of(user);
        when(userService.getAllUsers()).thenReturn(users);
        when(objectMapper.writeValueAsString(users)).thenReturn(jsonArray);
        when(response.getWriter()).thenReturn(writer);

        usersServlet.doGet(request, response);

        verify(request).getRequestURI();
        verify(userService).getAllUsers();
        verify(objectMapper).writeValueAsString(users);
        verify(writer).write(jsonArray);
        verify(response).addHeader("Accept", "application/json; charset: UTF-8");
        verify(response).setStatus(200);
    }

    @Test
    void whenPost_thenReturn201UserCreated() throws IOException {
        when(request.getHeader("Requester-Username")).thenReturn(requestUsername);
        when(request.getInputStream()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(json.getBytes());
        when(objectMapper.readValue(json, UserDTO.class)).thenReturn(user);
        when(response.getWriter()).thenReturn(writer);

        usersServlet.doPost(request, response);

        verify(userService).createUser(eq(user), any(), eq(requestUsername));
        verify(response).setStatus(201);
        verify(writer).println("User created");
    }

    @Test
    void whenReadUserMappingException_thenThrowUserIllegalStateException() throws IOException {
        when(request.getHeader("Requester-Username")).thenReturn(requestUsername);
        when(request.getInputStream()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(json.getBytes());
        when(objectMapper.readValue(json, UserDTO.class)).thenThrow(JsonMappingException.class);

        assertThatThrownBy(() -> usersServlet.doPost(request, response))
                .isEqualTo(new ApplicationException(ExceptionResponses.REQUEST_OBJECT_VALIDATION_FAILURE, null));
    }

    @Test
    void whenReadUserProcessingException_thenThrowUserIllegalStateException() throws IOException {
        when(request.getHeader("Requester-Username")).thenReturn(requestUsername);
        when(request.getInputStream()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(json.getBytes());
        when(objectMapper.readValue(json, UserDTO.class)).thenThrow(JsonProcessingException.class);

        assertThatThrownBy(() -> usersServlet.doPost(request, response))
                .isEqualTo(new ApplicationException(ExceptionResponses.JSON_MALFORMED, null));
    }
}
