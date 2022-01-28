package com.endava.upskill.confservice.api.controller;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.endava.upskill.confservice.domain.model.user.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.endava.upskill.confservice.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {

    public static final String REQUESTER_HEADER = "Requester-Username";

    private final UserService userService;

    private final Clock clock;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> listAllUsers() {
        log.info("Listing all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserDetailedDto getUser(
            @PathVariable String username) {
        log.info("Getting user details: {}", username);
        return userService.getUser(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(
            @RequestBody
                    UserDto user,
            @RequestHeader(REQUESTER_HEADER)
            @AdminOnly
                    String requesterUsername) {
        log.info("Creating user: {}", user);
        userService.createUser(user, LocalDateTime.now(clock), requesterUsername);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable
                    String username,
            @RequestHeader(REQUESTER_HEADER)
            @AdminOnly
                    String requesterUsername) {
        log.info("Deleting user: {}", username);
        userService.deleteUser(username, requesterUsername);
    }
}
