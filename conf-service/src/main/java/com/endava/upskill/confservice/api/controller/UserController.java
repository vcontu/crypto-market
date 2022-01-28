package com.endava.upskill.confservice.api.controller;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.endava.upskill.confservice.config.AdminConfig;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.user.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.endava.upskill.confservice.domain.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final Clock clock;

    public ApiResponse<List<UserDto>> listAllUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
        return new ApiResponse<>(HttpServletResponse.SC_OK, allUsers);
    }

    public ApiResponse<UserDetailedDto> getUser(String userId) {
        UserDetailedDto user = userService.getUser(userId);
        return new ApiResponse<>(HttpServletResponse.SC_OK, user);
    }

    public ApiResponse<Void> createUser(UserDto user, final String requesterUsername) {
        requireRequesterAdmin(requesterUsername);

        userService.createUser(user, LocalDateTime.now(clock), requesterUsername);
        return new ApiResponse<>(HttpServletResponse.SC_CREATED);
    }

    public ApiResponse<Void> deleteUser(String userId, String requesterUsername) {
        requireRequesterAdmin(requesterUsername);

        userService.deleteUser(userId, requesterUsername);
        return new ApiResponse<>(HttpServletResponse.SC_NO_CONTENT);
    }

    private void requireRequesterAdmin(String requesterUsername) {
        if (!requesterUsername.equals(AdminConfig.ADMIN_USERNAME)) {
            throw DomainException.ofAuthorizationFailure(requesterUsername);
        }
    }
}
