package com.endava.upskill.confservice.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.endava.upskill.confservice.domain.dao.UserRepository;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.user.Status;
import com.endava.upskill.confservice.domain.model.user.User;
import com.endava.upskill.confservice.domain.model.user.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.endava.upskill.confservice.util.Tokens;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final User user = new User(Tokens.USERNAME, Tokens.EMAIL, Status.ACTIVE, Tokens.LDT, Tokens.LDT, Tokens.REQUESTER_USERNAME);

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

        assertThatNoException().isThrownBy(() -> userService.deleteUser(Tokens.USERNAME, Tokens.USERNAME_ADMIN));
    }

    @Test
    void whenDeleteNonExistingUser_thenThrowUserNotFoundException() {
        when(userRepository.delete(Tokens.USERNAME)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(Tokens.USERNAME, Tokens.USERNAME_ADMIN)).isEqualTo(DomainException.ofUserNotFound(Tokens.USERNAME));
    }

    @Test
    void whenDeleteAdminUser_thenThrowDeleteUserForbidden() {
        assertThatThrownBy(() -> userService.deleteUser(Tokens.USERNAME_ADMIN, Tokens.USERNAME_ADMIN)).isEqualTo(DomainException.ofUserNotRemovable(Tokens.USERNAME_ADMIN));
        verifyNoInteractions(userRepository);

    }

    @Test
    void whenCreateUserWithInactvUser_thenThrowInactvUserCreation() {
        UserDto userDto = new UserDto(Tokens.USERNAME, Tokens.EMAIL, Status.INACTV);

        assertThatThrownBy(() -> userService.createUser(userDto, Tokens.LDT, Tokens.REQUESTER_USERNAME))
                .isEqualTo(DomainException.ofUserValidationStatus());

        verifyNoInteractions(userRepository);
    }

    @ParameterizedTest
    @MethodSource("validationFailureUser")
    void whenCreateUserWithMissingPropertiesUser_thenThrowInactvUserCreation(UserDto userDto, DomainException appException) {
        assertThatThrownBy(() -> userService.createUser(userDto, Tokens.LDT, Tokens.REQUESTER_USERNAME))
                .isEqualTo(appException);

        verifyNoInteractions(userRepository);
    }

    public static Stream<Arguments> validationFailureUser() {
        return Stream.of(
                Arguments.of(new UserDto(null, Tokens.EMAIL, Status.ACTIVE), DomainException.ofUserValidationUsername()),
                Arguments.of(new UserDto(Tokens.USERNAME, null, Status.ACTIVE), DomainException.ofUserValidationEmail()),
                Arguments.of(new UserDto(Tokens.USERNAME, Tokens.EMAIL, null), DomainException.ofUserValidationStatus())
        );
    }

    @Test
    void whenCreateUser_thenNoExceptionThrown() {
        when(userRepository.save(user)).thenReturn(true);

        assertThatNoException().isThrownBy(() -> userService.createUser(UserDto.fromUser(user), Tokens.LDT, Tokens.REQUESTER_USERNAME));

        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
    }

    @Test
    void whenCreateUserWithExistentUsername_thenThrowUserAlreadyExistentException() {
        when(userRepository.save(user)).thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(UserDto.fromUser(user), Tokens.LDT, Tokens.REQUESTER_USERNAME))
                .isEqualTo(DomainException.ofUserAlreadyExists(user.getUsername()));
    }

    @Test
    void whenGetRequester_thenReturnOptionalFromRepository() {
        when(userRepository.get(Tokens.USERNAME)).thenReturn(Optional.of(user));

        Optional<User> actualUser = userService.getRequester(Tokens.USERNAME);

        assertThat(actualUser).contains(user);
    }
}
