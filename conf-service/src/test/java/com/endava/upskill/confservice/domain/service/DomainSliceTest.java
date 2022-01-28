package com.endava.upskill.confservice.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

import com.endava.upskill.confservice.application.ValidationConfig;
import com.endava.upskill.confservice.domain.dao.UserRepository;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.endava.upskill.confservice.domain.model.user.Status;
import com.endava.upskill.confservice.domain.model.user.User;
import com.endava.upskill.confservice.domain.model.user.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.endava.upskill.confservice.persistence.InMemUserRepository;
import com.endava.upskill.confservice.util.Tokens;

import static com.endava.upskill.confservice.util.Tokens.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ValidationConfig.class, UserServiceImpl.class, InMemUserRepository.class})
class DomainSliceTest {

    private final User user = new User(USERNAME, EMAIL, Status.ACTIVE, LDT, LDT, USERNAME_ADMIN);

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private UserService userService;

    @AfterEach
    void tearDown() {
        usersRepo.getAll().stream()
                .map(User::getUsername)
                .forEach(usersRepo::delete);
    }

    @Test
    void whenGetAllUsers_thenReturnAllUsersFromRepository() {
        List<User> userList = List.of(this.user);
        userList.forEach(usersRepo::save);

        List<UserDto> receivedList = userService.getAllUsers();

        assertThat(receivedList).containsExactly(UserDto.fromUser(user));
    }

    @Test
    void whenGetExistentUser_thenReturnUserFromRepository() {
        usersRepo.save(user);

        UserDetailedDto receivedUser = userService.getUser(USERNAME);

        assertThat(receivedUser).isEqualTo(UserDetailedDto.fromUser(user));
    }

    @Test
    void whenGetNonexistentUser_thenThrowUserNotFoundException() {
        assertThatThrownBy(() -> userService.getUser(USERNAME))
                .isEqualTo(DomainException.ofUserNotFound(USERNAME));
    }

    @Test
    void whenDeleteExistentUser_thenNoExceptionThrown() {
        usersRepo.save(user);
        assertThatNoException().isThrownBy(() -> userService.deleteUser(USERNAME, USERNAME_ADMIN));
    }

    @Test
    void whenDeleteNonExistingUser_thenThrowUserNotFoundException() {
        assertThatThrownBy(() -> userService.deleteUser(USERNAME, USERNAME_ADMIN))
                .isEqualTo(DomainException.ofUserNotFound(USERNAME));
    }

    @Test
    void whenDeleteAdminUser_thenThrowDeleteUserForbidden() {
        assertConstraintViolationException(() -> userService.deleteUser(USERNAME_ADMIN, USERNAME_ADMIN), ExceptionResponse.USER_NOT_REMOVABLE);
    }

    @ParameterizedTest
    @MethodSource("validationFailureUser")
    void whenCreateUserWithMissingPropertiesUser_thenThrowInactvUserCreation(UserDto userDto, ExceptionResponse exceptionResponse) {
        assertConstraintViolationException(() -> userService.createUser(userDto, Tokens.LDT, Tokens.USERNAME_ADMIN), exceptionResponse);
    }

    public static Stream<Arguments> validationFailureUser() {
        return Stream.of(
                Arguments.of(new UserDto("123blalb", Tokens.EMAIL, Status.ACTIVE), ExceptionResponse.USER_VALIDATION_USERNAME),
                Arguments.of(new UserDto(null, Tokens.EMAIL, Status.ACTIVE), ExceptionResponse.USER_VALIDATION_USERNAME),
                Arguments.of(new UserDto(Tokens.USERNAME, null, Status.ACTIVE), ExceptionResponse.USER_VALIDATION_EMAIL),
                Arguments.of(new UserDto(Tokens.USERNAME, "thisNotAMail", Status.ACTIVE), ExceptionResponse.USER_VALIDATION_EMAIL),
                Arguments.of(new UserDto(Tokens.USERNAME, Tokens.EMAIL, null), ExceptionResponse.USER_VALIDATION_STATUS),
                Arguments.of(new UserDto(Tokens.USERNAME, Tokens.EMAIL, Status.INACTV), ExceptionResponse.USER_VALIDATION_STATUS)
        );
    }

    @Test
    void whenCreateUser_thenNoExceptionThrown() {
        assertThatNoException().isThrownBy(() -> userService.createUser(UserDto.fromUser(user), LDT, USERNAME_ADMIN));

        final Optional<User> userOptional = usersRepo.get(USERNAME);
        assertThat(userOptional).contains(this.user);
    }

    @Test
    void whenCreateUserWithExistentUsername_thenThrowUserAlreadyExistentException() {
        usersRepo.save(user);

        assertThatThrownBy(() -> userService.createUser(UserDto.fromUser(user), LDT, USERNAME_ADMIN))
                .isEqualTo(DomainException.ofUserAlreadyExists(user.getUsername()));
    }

    @Test
    void whenGetRequester_thenReturnOptionalFromRepository() {
        usersRepo.save(user);

        Optional<User> actualUser = userService.getRequester(USERNAME);

        assertThat(actualUser).contains(user);
    }

    private void assertConstraintViolationException(ThrowableAssert.ThrowingCallable throwingCallable, ExceptionResponse exceptionResponse) {
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(ConstraintViolationException.class)
                .extracting("constraintViolations", as(InstanceOfAssertFactories.ITERABLE))
                .element(0)
                .extracting("constraintDescriptor")
                .extracting("attributes", as(InstanceOfAssertFactories.MAP))
                .contains(Map.entry("exceptionResponse", exceptionResponse));
    }
}
