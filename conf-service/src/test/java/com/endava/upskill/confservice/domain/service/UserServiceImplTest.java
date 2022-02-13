package com.endava.upskill.confservice.domain.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.domain.dao.UserRepository;
import com.endava.upskill.confservice.domain.model.create.UserDto;
import com.endava.upskill.confservice.domain.model.entity.Status;
import com.endava.upskill.confservice.domain.model.entity.User;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.get.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.update.UserUpdateDto;
import com.endava.upskill.confservice.util.Tokens;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final User user = new User(Tokens.USERNAME, Tokens.EMAIL, Status.ACTIVE, Tokens.LDT, Tokens.LDT, Tokens.USERNAME_ADMIN);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test
    void whenGetAllUsers_thenReturnAllUsersFromRepository() {
        List<User> userList = List.of(this.user);
        when(userRepository.getAll()).thenReturn(userList);

        List<UserDto> receivedList = userService.getAllUsers();

        assertThat(receivedList).containsExactly(UserDto.fromUser(user));
    }

    @Test
    void whenGetExistentUser_thenReturnUserFromRepository() {
        when(userRepository.get(Tokens.USERNAME)).thenReturn(Optional.of(user));

        UserDetailedDto receivedUser = userService.getUser(Tokens.USERNAME);

        assertThat(receivedUser).isEqualTo(UserDetailedDto.fromUser(user));
    }

    @Test
    void whenGetNonexistentUser_thenThrowUserNotFoundException() {
        when(userRepository.get(Tokens.USERNAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(Tokens.USERNAME))
                .isEqualTo(DomainException.ofUserNotFound(Tokens.USERNAME));
    }

    @Test
    void whenDeleteExistentUser_thenNoExceptionThrown() {
        when(userRepository.delete(Tokens.USERNAME)).thenReturn(true);

        assertThatNoException().isThrownBy(() -> userService.deleteUser(Tokens.USERNAME_ADMIN, Tokens.USERNAME));
    }

    @Test
    void whenDeleteNonExistingUser_thenThrowUserNotFoundException() {
        when(userRepository.delete(Tokens.USERNAME)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(Tokens.USERNAME_ADMIN, Tokens.USERNAME))
                .isEqualTo(DomainException.ofUserNotFound(Tokens.USERNAME));
    }

    @Test
    void whenCreateUser_thenNoExceptionThrown() {
        when(userRepository.save(user)).thenReturn(true);

        assertThatNoException().isThrownBy(() -> userService.createUser(Tokens.USERNAME_ADMIN, UserDto.fromUser(user), Tokens.LDT));

        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
    }

    @Test
    void whenCreateUserWithExistentUsername_thenThrowUserAlreadyExistentException() {
        when(userRepository.save(user)).thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(Tokens.USERNAME_ADMIN, UserDto.fromUser(user), Tokens.LDT))
                .isEqualTo(DomainException.ofUserAlreadyExists(user.getUsername()));
    }

    @Test
    void whenGetRequester_thenReturnOptionalFromRepository() {
        when(userRepository.get(Tokens.USERNAME)).thenReturn(Optional.of(user));

        Optional<User> actualUser = userService.getRequester(Tokens.USERNAME);

        assertThat(actualUser).contains(user);
    }

    @Test
    void whenUpdateUser_thenNoExceptionThrown() {
        final String email = "newmail@mail.com";
        final UserUpdateDto updateDto = new UserUpdateDto(Tokens.USERNAME, email, Status.SUSPND);
        final User originalUser = new User(Tokens.USERNAME, Tokens.EMAIL, Status.ACTIVE, Tokens.LDT, Tokens.LDT, Tokens.USERNAME_ADMIN);
        final User updatedUser = new User(Tokens.USERNAME, email, Status.SUSPND, Tokens.LDT, Tokens.LDT.plusHours(1), Tokens.USERNAME_ADMIN);
        when(userRepository.get(Tokens.USERNAME)).thenReturn(Optional.of(originalUser));
        when(userRepository.update(updatedUser)).thenReturn(true);

        assertThatNoException().isThrownBy(() -> userService.updateUser(Tokens.USERNAME_ADMIN, Tokens.USERNAME, updateDto, Tokens.LDT.plusHours(1)));
    }

    @Test
    void whenUpdateUserWasConcurrentlyRemoved_thenUserNotFound() {
        final UserUpdateDto updateDto = new UserUpdateDto(Tokens.USERNAME, null, Status.SUSPND);
        final User originalUser = new User(Tokens.USERNAME, Tokens.EMAIL, Status.ACTIVE, Tokens.LDT, Tokens.LDT, Tokens.USERNAME_ADMIN);
        final User updatedUser = new User(Tokens.USERNAME, Tokens.EMAIL, Status.SUSPND, Tokens.LDT, Tokens.LDT.plusHours(1), Tokens.USERNAME_ADMIN);
        when(userRepository.get(Tokens.USERNAME)).thenReturn(Optional.of(originalUser));
        when(userRepository.update(updatedUser)).thenReturn(false);

        assertThatThrownBy(() -> userService.updateUser(Tokens.USERNAME_ADMIN, Tokens.USERNAME, updateDto, Tokens.LDT.plusHours(1)))
                .isEqualTo(DomainException.ofUserNotFound(Tokens.USERNAME));
    }

    @Test
    void whenUpdateUserNotExisting_thenUserNotFound() {
        final UserUpdateDto updateDto = new UserUpdateDto(Tokens.USERNAME, null, Status.SUSPND);

        when(userRepository.get(Tokens.USERNAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(Tokens.USERNAME_ADMIN, Tokens.USERNAME, updateDto, Tokens.LDT.plusHours(1)))
                .isEqualTo(DomainException.ofUserNotFound(Tokens.USERNAME));
    }

    @Test
    void whenUpdateUserInactv_thenUpdateUserInactvException() {
        final UserUpdateDto updateDto = new UserUpdateDto(Tokens.USERNAME, null, Status.SUSPND);
        final User originalUser = new User(Tokens.USERNAME, Tokens.EMAIL, Status.INACTV, Tokens.LDT, Tokens.LDT, Tokens.USERNAME_ADMIN);
        when(userRepository.get(Tokens.USERNAME)).thenReturn(Optional.of(originalUser));

        assertThatThrownBy(() -> userService.updateUser(Tokens.USERNAME_ADMIN, Tokens.USERNAME, updateDto, Tokens.LDT.plusHours(1)))
                .isEqualTo(DomainException.ofUpdatingInactvUser(Tokens.USERNAME));
    }
}
