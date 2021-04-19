package com.endava.internship.cryptomarket.confservice.business;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import com.endava.internship.cryptomarket.confservice.business.validator.UserValidator;
import com.endava.internship.cryptomarket.confservice.data.UserRepository;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.*;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.CLIENT;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.INACTV;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    private final String testUsername = "user";

    @BeforeEach
    void setUp() {
        testUser = User.builder().username("user").email("email@gmail.com")
                .role(OPERAT).status(ACTIVE).build();
    }

    @Test
    void whenGetAllUsers_thenReturnAllUsersFromRepository() {
        List<User> repositoryResponse = List.of(testUser);
        UserDTO[] expectedResponse = repositoryResponse.stream().map(UserDTO::of).toArray(UserDTO[]::new);
        when(userRepository.getAll()).thenReturn(repositoryResponse);

        List<UserDTO> receivedList = userService.getAllUsers();

        assertThat(receivedList).containsExactly(expectedResponse);
        verify(userRepository).getAll();
    }

    @Test
    void whenGetExistentUser_thenReturnUserFromRepository() {
        when(userRepository.get(testUsername)).thenReturn(Optional.of(testUser));

        UserDTO receivedUser = userService.getUser(testUsername);

        assertThat(receivedUser).isEqualTo(UserDTO.of(testUser));
        verify(userRepository).get(testUsername);
    }

    @Test
    void whenGetNonexistentUser_thenThrowUserNotFoundException() {
        when(userRepository.get(testUsername)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(testUsername))
                .isEqualTo(new ApplicationException(USER_NOT_FOUND, testUsername));
        verify(userRepository).get(testUsername);
    }

    @Test
    void whenDeleteExistentUser_thenNoExceptionThrown() {
        when(userRepository.delete(testUsername)).thenReturn(true);

        assertThatNoException().isThrownBy(() -> userService.deleteUser(testUsername));
        verify(userRepository).delete(testUsername);
    }

    @Test
    void whenDeleteNonexistentUser_thenThrowUserNotFoundException() {
        when(userRepository.delete(testUsername)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(testUsername)).isEqualTo(new ApplicationException(USER_NOT_FOUND, testUsername));
        verify(userRepository).delete(testUsername);
    }

    @Test
    void whenCreateUser_thenNoExceptionThrown() {
        LocalDateTime testDate = now();
        String requestUsername = "request_username";
        User requestUser = User.builder().username(requestUsername).build();
        UserDTO testUserDTO = UserDTO.of(testUser);
        when(userRepository.get(requestUsername)).thenReturn(Optional.of(requestUser));
        when(userRepository.get(testUsername)).thenReturn(Optional.empty());
        when(userRepository.save(testUser)).thenReturn(true);

        assertThatNoException().isThrownBy(() -> userService.createUser(testUserDTO, testDate, requestUsername));
        verify(userRepository).get(requestUsername);
        verify(userValidator).validateUserCreate(testUserDTO, requestUser);
        verify(userRepository).save(userCaptor.capture());
        verify(userRepository).get(testUsername);
        assertThat(userCaptor.getValue().getCreatedOn()).isEqualTo(testDate);
    }

    @Test
    void whenCreateUserWithExistentUsername_thenThrowUserAlreadyExistentException() {
        LocalDateTime testDate = now();
        String requestUsername = "request_username";
        User requestUser = User.builder().username(requestUsername).build();
        UserDTO testUserDTO = UserDTO.of(testUser);
        when(userRepository.get(requestUsername)).thenReturn(Optional.of(requestUser));
        when(userRepository.get(testUsername)).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService.createUser(testUserDTO, testDate, requestUsername))
                .isEqualTo(new ApplicationException(USER_ALREADY_EXISTS, testUser.getUsername()));

        verify(userValidator).validateUserCreate(testUserDTO, requestUser);
        verify(userRepository).get(testUsername);
        verify(userRepository).get(requestUsername);
    }

    @Test
    void whenAmendExistentUser_thenUserModifiedAndNoExceptionThrown() {
        LocalDateTime testDate = now();
        User oldUser = User.builder().username(testUsername).email("anotherEmail@gmail.com")
                .role(CLIENT).status(INACTV).build();
        String requestUsername = "request_username";
        User requestUser = User.builder().username(requestUsername).build();
        final String newEmail = "newMail@gmail.com";
        UserDTO modifiedUser = new UserDTO(testUsername, newEmail, CLIENT, INACTV,
                null, null, null);
        when(userRepository.get(testUsername)).thenReturn(Optional.of(testUser));
        when(userRepository.get(requestUsername)).thenReturn(Optional.of(requestUser));

        assertThatNoException().isThrownBy(() -> userService.amendUser(testUsername, modifiedUser, requestUsername, testDate));

        verify(userRepository).get(testUsername);
        verify(userRepository).get(requestUsername);
        verify(userValidator).validateUserAmend(oldUser, modifiedUser, requestUser);
        assertThat(testUser.getRole()).isEqualTo(CLIENT);
        assertThat(testUser.getStatus()).isEqualTo(INACTV);
        assertThat(testUser.getEmail()).isEqualTo(newEmail);
        assertThat(testUser.getUpdatedOn()).isEqualTo(testDate);
        assertThat(testUser.getUpdatedBy()).isEqualTo(requestUsername);
    }

    @Test
    void whenAmendNonexistentUser_thenThrowUserNotFoundException() {
        LocalDateTime testDate = now();
        String requestUsername = "request_username";
        when(userRepository.get(requestUsername)).thenReturn(Optional.of(testUser));
        when(userRepository.get(testUsername)).thenReturn(Optional.empty());
        UserDTO modifiedUser = UserDTO.of(testUser);

        assertThatThrownBy(() -> userService.amendUser(testUsername, modifiedUser, requestUsername, testDate))
                .isEqualTo(new ApplicationException(USER_NOT_FOUND, testUsername));

        verify(userRepository).get(testUsername);
        verify(userRepository).get(requestUsername);
    }

    @Test
    void whenAmendUserWithNoChanges_thenThrowUserNotChangedException() {
        LocalDateTime testDate = now();
        User oldUser = User.builder().username(testUsername).email("anotherEmail@gmail.com")
                .role(CLIENT).status(INACTV).build();
        String requestUsername = "request_username";
        User requestUser = User.builder().username(requestUsername).build();
        UserDTO modifiedUser = UserDTO.of(testUser);
        when(userRepository.get(testUsername)).thenReturn(Optional.of(testUser));
        when(userRepository.get(requestUsername)).thenReturn(Optional.of(requestUser));

        assertThatThrownBy(() -> userService.amendUser(testUsername, modifiedUser, requestUsername, testDate))
                .isEqualTo(new ApplicationException(USER_NOT_CHANGED, null));

        verify(userRepository).get(testUsername);
        verify(userRepository).get(requestUsername);
        verify(userValidator).validateUserAmend(oldUser, modifiedUser, requestUser);
    }

    @Test
    void whenGetRequester_thenReturnOptionalFromRepository() {
        when(userRepository.get(testUsername)).thenReturn(Optional.ofNullable(testUser));

        Optional<User> actualUser = userService.getRequester(testUsername);

        assertThat(actualUser.get()).isEqualTo(testUser);

    }
}
