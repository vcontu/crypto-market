package com.endava.internship.cryptomarket.confservice.api.controllers;

import com.endava.internship.cryptomarket.confservice.business.UserService;
import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.ADMIN;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private final String username = "operat";

    final User user = User.builder().username("user").email("user@gmail.com").role(ADMIN).status(ACTIVE).build();

    private final Optional<User> requestUser = Optional.of(user);

    private final UserDto userDto = new UserDto(username, "operat@gmail.com", OPERAT,
            ACTIVE, null, null, null);
    ;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {

    }

    @Test
    void whenGetUsers_thenReturnUsersFromService() {
        when(userService.getAllUsers(user)).thenReturn(List.of(userDto));

        List<UserDto> users = userController.getUsers(requestUser);

        assertThat(users).containsExactly(userDto);
        verify(userService).getAllUsers(user);
    }

    @Test
    void whenGetUser_thenReturnUserFromService() {
        when(userService.getUser(username, user)).thenReturn(userDto);

        UserDto actualUser = userController.getUser(username, requestUser);

        assertThat(actualUser).isEqualTo(userDto);
        verify(userService).getUser(username, user);
    }

    @Test
    void whenPostUser_thenReturnUserCreated() {
        UserDto userDTO = new UserDto("user", null, null,
                null, null, null, null);

        String response = userController.postUser(userDTO, requestUser);

        assertThat(response).isEqualTo("User created");
        verify(userService).createUser(eq(userDTO), eq(user));
    }

    @Test
    void whenPutUser_thenReturnAccepted() {
        UserDto userDTO = new UserDto("user", null, null,
                null, null, null, null);

        String response = userController.putUser(username, userDTO, requestUser);

        assertThat(response).isEqualTo("Accepted");
        verify(userService).amendUser(eq(username), eq(userDTO), eq(user));
    }

    @Test
    void whenPuteleteUser_thenCallServiceDelete() {
        userController.deleteUser(username, requestUser);

        verify(userService).deleteUser(eq(username), eq(user));
    }

}
