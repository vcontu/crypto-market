package com.endava.upskill.confservice.api.servlet;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.nonNull;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.endava.upskill.confservice.api.annotation.ServletComponent;
import com.endava.upskill.confservice.api.controller.ApiResponse;
import com.endava.upskill.confservice.api.controller.UserController;
import com.endava.upskill.confservice.api.http.Endpoint;
import com.endava.upskill.confservice.api.http.EndpointMatcher;
import com.endava.upskill.confservice.api.http.RequestDetails;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.endava.upskill.confservice.api.http.HttpHeaders.APPLICATION_JSON;
import static com.endava.upskill.confservice.api.http.HttpHeaders.REQUESTER_USERNAME;

import lombok.RequiredArgsConstructor;

@ServletComponent(path = "/*")
@RequiredArgsConstructor
public class UsersServlet extends HttpServlet {

    private final UserController userController;

    private final EndpointMatcher endpointMatcher;

    private final ObjectMapper objectMapper;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        final String requesterUsername = req.getHeader(REQUESTER_USERNAME);
        final String requestPath = req.getRequestURI();
        final String method = req.getMethod();
        final RequestDetails requestDetails = endpointMatcher.matchEndpoint(method, requestPath);
        final Endpoint endpoint = requestDetails.endpoint();

        final ApiResponse<?> apiResponse = switch (endpoint) {
            case LIST_USERS -> userController.listAllUsers();
            case GET_USER -> userController.getUser(requestDetails.extractedRef());
            case CREATE_USER -> {
                UserDto userDto = readServletRequestBody(req);
                yield userController.createUser(userDto, requesterUsername);
            }
            case DELETE_USER -> userController.deleteUser(requestDetails.extractedRef(), requesterUsername);
        };

        if (nonNull(apiResponse.body())) {
            writeServletResponseBody(resp, apiResponse.body());
        }
        resp.setStatus(apiResponse.statusCode());
    }

    private UserDto readServletRequestBody(HttpServletRequest req) {
        if (!APPLICATION_JSON.equals(req.getContentType())) {
            throw DomainException.ofNotAcceptableContent();
        }

        try {
            String jsonBody = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonBody, UserDto.class);
        } catch (JsonMappingException e) {
            throw DomainException.ofRequestObjectValidation();
        } catch (JsonProcessingException e) {
            throw DomainException.ofJsonMalformed();
        } catch (IOException e) {
            throw DomainException.ofInternalServerError();
        }
    }

    private void writeServletResponseBody(HttpServletResponse req, Object body) {
        req.setContentType(APPLICATION_JSON);
        try {
            String serializedJson = objectMapper.writeValueAsString(body);
            Writer writer = req.getWriter();
            writer.write(serializedJson);
        } catch (IOException e) {
            //failed to serialize jackson
            throw DomainException.ofInternalServerError();
        }
    }
}
