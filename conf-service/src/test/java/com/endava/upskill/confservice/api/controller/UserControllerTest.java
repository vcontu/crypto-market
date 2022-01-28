package com.endava.upskill.confservice.api.controller;

import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.user.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.endava.upskill.confservice.domain.service.UserService;
import com.endava.upskill.confservice.util.Tokens;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserDto userDto;

    @Mock
    private UserDetailedDto userDetailedDto;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService, Tokens.CLOCK_FIXED);
    }

    @Test
    void listAllUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(userDto));

        ApiResponse<List<UserDto>> listApiResponse = userController.listAllUsers();

        var expectedApiResponse = new ApiResponse<>(SC_OK, List.of(userDto));
        assertThat(listApiResponse).isEqualTo(expectedApiResponse);
    }

    @Test
    void getUser() {
        when(userService.getUser(Tokens.USERNAME)).thenReturn(userDetailedDto);

        ApiResponse<UserDetailedDto> apiResponse = userController.getUser(Tokens.USERNAME);

        assertThat(apiResponse).isEqualTo(new ApiResponse<>(SC_OK, userDetailedDto));
    }

    @Test
    void createUser() {
        ApiResponse<Void> apiResponse = userController.createUser(userDto, Tokens.USERNAME_ADMIN);

        assertThat(apiResponse).isEqualTo(new ApiResponse<>(SC_CREATED));

        verify(userService).createUser(userDto, Tokens.LDT, Tokens.USERNAME_ADMIN);
    }

    @Test
    void createUser_authorizationFailed() {
        assertThatThrownBy(() -> userController.createUser(userDto, Tokens.USERNAME))
                .isEqualTo(DomainException.ofAuthorizationFailure(Tokens.USERNAME));

        verifyNoInteractions(userService);
    }

    @Test
    void deleteUser() {
        ApiResponse<Void> apiResponse = userController.deleteUser(Tokens.USERNAME, Tokens.USERNAME_ADMIN);

        assertThat(apiResponse).isEqualTo(new ApiResponse<>(SC_NO_CONTENT));

        verify(userService).deleteUser(Tokens.USERNAME, Tokens.USERNAME_ADMIN);
    }

    @Test
    void deleteUser_authorizationFailed() {
        assertThatThrownBy(() -> userController.deleteUser(Tokens.USERNAME, Tokens.USERNAME))
                .isEqualTo(DomainException.ofAuthorizationFailure(Tokens.USERNAME));

        verifyNoInteractions(userService);
    }
}