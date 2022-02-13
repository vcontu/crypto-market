package com.endava.upskill.confservice.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

import com.endava.upskill.confservice.application.ValidationConfig;
import com.endava.upskill.confservice.domain.dao.UserRepository;
import com.endava.upskill.confservice.domain.model.create.UserDto;
import com.endava.upskill.confservice.domain.model.entity.Status;
import com.endava.upskill.confservice.domain.model.entity.User;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.endava.upskill.confservice.domain.model.get.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.update.UserUpdateDto;
import com.endava.upskill.confservice.persistence.InMemUserRepository;
import com.endava.upskill.confservice.provisioning.UserOnboarding;
import com.endava.upskill.confservice.util.Tokens;

import static com.endava.upskill.confservice.util.Tokens.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ValidationConfig.class, UserServiceImpl.class, InMemUserRepository.class})
class DomainSliceTest {

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private UserService userService;

    @RegisterExtension
    private final UserOnboarding onboarding = UserOnboarding.ofRandomUser().usingRepository().build();

    private final String username = onboarding.getUsername();

    private final UserDto userDto = onboarding.getUserDto();

    private final User user = onboarding.getUser();

    @Nested
    class CreateUserTests {
        @Nested
        class GivenUserToBeCreatedTest {

            @RegisterExtension
            private final UserOnboarding randomUser = UserOnboarding.ofRandomUser()
                    .usingRepository().cleanupOnly().build();

            @Test
            void whenCreateUser_thenNoExceptionThrown() {
                assertThatNoException().isThrownBy(() -> userService.createUser(USERNAME_ADMIN, randomUser.getUserDto(), LDT));

                final Optional<User> userOptional = usersRepo.get(randomUser.getUsername());
                assertThat(userOptional).contains(randomUser.getUser());
            }
        }

        @ParameterizedTest
        @MethodSource("validationFailureUser")
        void whenCreateUserWithWrongProperties_thenThrowUserValidation(UserDto userDto, ExceptionResponse exceptionResponse) {
            assertConstraintViolationException(() -> userService.createUser(Tokens.USERNAME_ADMIN, userDto, Tokens.LDT), exceptionResponse);
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
        void whenCreateUserWithExistentUsername_thenThrowUserAlreadyExistentException() {
            assertThatThrownBy(() -> userService.createUser(USERNAME_ADMIN, userDto, LDT))
                    .isEqualTo(DomainException.ofUserAlreadyExists(username));
        }
    }

    @Nested
    class GetUserTests {

        @Test
        void whenGetExistentUser_thenReturnUserFromRepository() {
            UserDetailedDto receivedUser = userService.getUser(username);
            assertThat(receivedUser).isEqualTo(UserDetailedDto.fromUser(user));
        }

        @Test
        void whenGetAllUsers_thenReturnAllUsersFromRepository() {
            List<UserDto> receivedList = userService.getAllUsers();
            assertThat(receivedList).containsExactly(userDto);
        }

        @Test
        void whenGetNonexistentUser_thenThrowUserNotFoundException() {
            assertThatThrownBy(() -> userService.getUser(USERNAME_NOT_EXISTING))
                    .isEqualTo(DomainException.ofUserNotFound(USERNAME_NOT_EXISTING));
        }

        @Test
        void whenGetRequester_thenReturnOptionalFromRepository() {
            Optional<User> actualUser = userService.getRequester(username);
            assertThat(actualUser).contains(user);
        }
    }

    @Nested
    class UpdateUserTests {
        @Test
        void whenUpdateUser_thenNoExceptionThrown() {
            final String updatedEmail = "another@email.com";
            final UserUpdateDto userUpdateDto = new UserUpdateDto(username, updatedEmail, null);

            assertThatNoException().isThrownBy(() -> userService.updateUser(username, username, userUpdateDto, LDT.plusMinutes(10)));

            final Optional<User> userOptional = usersRepo.get(username);
            assertThat(userOptional).contains(new User(username, updatedEmail, Status.SUSPND, LDT, LDT.plusMinutes(10), USERNAME));
        }

        @Test
        void whenUpdateUserAdmin_thenThrowNotUpdatable() {
            final UserUpdateDto userUpdateDto = new UserUpdateDto(USERNAME_ADMIN, EMAIL, null);
            assertConstraintViolationException(() -> userService.updateUser(USERNAME_ADMIN, USERNAME_ADMIN, userUpdateDto, LDT), ExceptionResponse.USER_NOT_UPDATABLE);
        }

        @Test
        void whenUpdateUserWithNoProperties_thenThrowAtLeastOnePropertyMustBeUpdated() {
            final UserUpdateDto userUpdateDto = new UserUpdateDto(username, null, null);
            assertConstraintViolationException(() -> userService.updateUser(USERNAME_ADMIN, username, userUpdateDto, LDT),
                    ExceptionResponse.USER_UPDATE_NO_PROPERTIES);
        }

        @Test
        void whenUpdateUserWithInvalidEmail_thenThrowUserValidationEmail() {
            final UserUpdateDto userUpdateDto = new UserUpdateDto(username, INVALID_EMAIL, null);
            assertConstraintViolationException(() -> userService.updateUser(USERNAME_ADMIN, username, userUpdateDto, LDT),
                    ExceptionResponse.USER_VALIDATION_EMAIL);
        }

        @Test
        void whenUpdateUserWithNoUsername_thenThrowAtLeastOnePropertyMustBeUpdated() {
            final UserUpdateDto userUpdateDto = new UserUpdateDto(null, EMAIL, null);
            assertConstraintViolationException(() -> userService.updateUser(USERNAME_ADMIN, username, userUpdateDto, LDT),
                    ExceptionResponse.USERNAME_DIFFERENT);
        }

        @Test
        void whenUpdateUserDifferentFromRequest_thenThrowUsernameDifferent() {
            final UserUpdateDto userUpdateDto = new UserUpdateDto(USERNAME_NOT_EXISTING, EMAIL, null);
            assertConstraintViolationException(() -> userService.updateUser(USERNAME_ADMIN, username, userUpdateDto, LDT),
                    ExceptionResponse.USERNAME_DIFFERENT);
        }

        @Test
        void whenUpdateUserMultipleExceptions_thenThrowThePriorityOne() {
            final UserUpdateDto userUpdateDto = new UserUpdateDto(username, INVALID_EMAIL, null);
            assertConstraintViolationException(() -> userService.updateUser(USERNAME_ADMIN, USERNAME_NOT_EXISTING, userUpdateDto, LDT),
                    ExceptionResponse.USERNAME_DIFFERENT);
        }

        @Test
        void whenUpdateUserThatDoesNotExist_thenThrowUserNotFound() {
            final UserUpdateDto userUpdateDto = new UserUpdateDto(USERNAME_NOT_EXISTING, null, Status.SUSPND);
            assertThatThrownBy(() -> userService.updateUser(USERNAME_ADMIN, USERNAME_NOT_EXISTING, userUpdateDto, LDT))
                    .isEqualTo(DomainException.ofUserNotFound(USERNAME_NOT_EXISTING));

        }

        @Test
        void whenUpdateUserThatIsInactv_thenThrowUserInactv() {
            usersRepo.update(new User(username, EMAIL, Status.INACTV,LDT, LDT, USERNAME_ADMIN));
            final UserUpdateDto userUpdateDto = new UserUpdateDto(username, null, Status.SUSPND);
            assertThatThrownBy(() -> userService.updateUser(USERNAME_ADMIN, username, userUpdateDto, LDT))
                    .isEqualTo(DomainException.ofUpdatingInactvUser(username));
        }
    }

    @Nested
    class DeleteUserTests {

        @Test
        void whenDeleteNonExistingUser_thenThrowUserNotFoundException() {
            assertThatThrownBy(() -> userService.deleteUser(USERNAME_ADMIN, USERNAME_NOT_EXISTING))
                    .isEqualTo(DomainException.ofUserNotFound(USERNAME_NOT_EXISTING));
        }

        @Test
        void whenDeleteAdminUser_thenThrowDeleteUserForbidden() {
            assertConstraintViolationException(() -> userService.deleteUser(USERNAME_ADMIN, USERNAME_ADMIN),
                    ExceptionResponse.USER_NOT_REMOVABLE);
        }

        @Test
        void whenDeleteExistentUser_thenNoExceptionThrown() {
            assertThatNoException().isThrownBy(() -> userService.deleteUser(USERNAME_ADMIN, username));
        }

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
