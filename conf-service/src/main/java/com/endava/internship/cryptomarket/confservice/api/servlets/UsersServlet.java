package com.endava.internship.cryptomarket.confservice.api.servlets;

import com.endava.internship.cryptomarket.confservice.api.annotations.ServletComponent;
import com.endava.internship.cryptomarket.confservice.business.UserService;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.time.LocalDateTime.now;

@ServletComponent(path = "/users/*")
@RequiredArgsConstructor
public class UsersServlet extends HttpServlet {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object result = prepareUsers(req);
        String response = objectMapper.writeValueAsString(result);

        prepareResponse(resp, response);
    }

    private Object prepareUsers(HttpServletRequest req) {
        String[] path = req.getRequestURI().split("/");
        if (path.length == 4) {
            final String username = path[3];
            return getUser(username);
        } else {
            return getAllUsers();
        }
    }

    private UserDTO getUser(String username) {
        return userService.getUser(username);
    }

    private List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    public void prepareResponse(HttpServletResponse resp, String response) throws IOException {
        Writer writer = resp.getWriter();
        writer.write(response);
        resp.addHeader("Accept", "application/json; charset: UTF-8");
        resp.setStatus(200);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestUsername = req.getHeader("Requester-Username");
        UserDTO userDTO = readUser(req);

        userService.createUser(userDTO, now(), requestUsername);

        resp.setStatus(201);
        resp.getWriter().println("User created");
    }

    private UserDTO readUser(HttpServletRequest req) throws IOException {
        InputStream input = req.getInputStream();
        String requestBody = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        try {
            return objectMapper.readValue(requestBody, UserDTO.class);
        } catch (JsonMappingException e) {
            throw new ApplicationException(ExceptionResponses.REQUEST_OBJECT_VALIDATION_FAILURE, null);
        } catch (JsonProcessingException e) {
            throw new ApplicationException(ExceptionResponses.JSON_MALFORMED, null);
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] path = req.getRequestURI().split("/");
        String userToChange = path[3];
        String requestUsername = req.getHeader("Requester-Username");

        UserDTO userDTO = readUser(req);

        if (!userToChange.equals(userDTO.getUsername())) {
            throw new ApplicationException(ExceptionResponses.DIFFERENT_USERNAME, null);
        }

        userService.amendUser(userToChange, userDTO, requestUsername, now());

        resp.setStatus(202);
        resp.getWriter().println("Accepted");
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String[] path = req.getRequestURI().split("/");
        String username = path[3];

        userService.deleteUser(username);

        resp.setStatus(204);
    }
}
