package com.endava.internship.cryptomarket.confservice.business.validator;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.*;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.ADMIN;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.*;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserValidatorImplTest {

    private UserValidator userValidator;

    private User requestUser;
    private UserDTO newUser;
    private User oldUser;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidatorImpl();
        requestUser = User.builder().username("request_username").build();
        newUser = new UserDTO();
        newUser.setUsername("username");
        oldUser = User.builder().username("user").build();

    }

    @Test
    void whenValidateAdminUserChangeAdmin_thenThrowAdminNotAllowedChangeAdmin() {
        requestUser.setRole(ADMIN);
        oldUser.setRole(ADMIN);

        assertThatThrownBy(() -> userValidator.validateUserChange(oldUser, requestUser))
                .isEqualTo(new ApplicationException(ADMIN_NOT_ALLOWED_CHANGE_ADMIN, requestUser.getUsername()));
    }

    @Test
    void whenValidateOperatUserChangeAdmin_thenThrowAdminNotAllowedChangeAdmin() {
        requestUser.setRole(OPERAT);
        oldUser.setRole(ADMIN);

        assertThatThrownBy(() -> userValidator.validateUserChange(oldUser, requestUser))
                .isEqualTo(new ApplicationException(OPERAT_NOT_ALLOWED_CHANGE_ADMIN_OPERAT, requestUser.getUsername()));
    }

    @Test
    void whenValidateOperatUserChangeOperat_thenThrowAdminNotAllowedChangeAdmin() {
        requestUser.setRole(OPERAT);
        oldUser.setRole(OPERAT);

        assertThatThrownBy(() -> userValidator.validateUserChange(oldUser, requestUser))
                .isEqualTo(new ApplicationException(OPERAT_NOT_ALLOWED_CHANGE_ADMIN_OPERAT, requestUser.getUsername()));
    }

    @Test
    void whenValidateAdminUserChangeOperat_thenNoExceptionThrow() {
        requestUser.setRole(ADMIN);
        oldUser.setRole(OPERAT);

        assertThatNoException().isThrownBy(() -> userValidator.validateUserChange(oldUser, requestUser));
    }

    @Test
    void whenValidateInactvUserAmend_thenThrowInactvUserAmend() {
        requestUser.setRole(ADMIN);
        oldUser.setRole(OPERAT);
        oldUser.setStatus(INACTV);

        assertThatThrownBy(() -> userValidator.validateUserAmend(oldUser, newUser, requestUser))
                .isEqualTo(new ApplicationException(INACTIV_USER_AMEND, oldUser.getUsername()));
    }

    @Test
    void whenValidateUserAmendWithOnlyUsername_thenThrowUserNotChanged() {
        requestUser.setRole(ADMIN);
        oldUser.setRole(OPERAT);

        assertThatThrownBy(() -> userValidator.validateUserAmend(oldUser, newUser, requestUser))
                .isEqualTo(new ApplicationException(USER_NOT_CHANGED, null));
    }

    @Test
    void whenValidateUserAmendWithInvalidUsername_thenThrowUserUserIllegalState() {
        requestUser.setRole(ADMIN);
        oldUser.setRole(OPERAT);
        UserDTO newUser1 = new UserDTO();
        newUser1.setRole(OPERAT);
        newUser1.setUsername("us");

        assertThatThrownBy(() -> userValidator.validateUserAmend(oldUser, newUser1, requestUser))
                .isEqualTo(new ApplicationException(USER_ILLEGAL_STATE, null));
    }

    @Test
    void whenValidateUserAmendWithInvalidEmail_thenThrowUserUserIllegalState() {
        requestUser.setRole(ADMIN);
        oldUser.setRole(OPERAT);
        UserDTO newUser1 = new UserDTO();
        newUser1.setRole(OPERAT);
        newUser1.setUsername("username");
        newUser1.setEmail("mail");

        assertThatThrownBy(() -> userValidator.validateUserAmend(oldUser, newUser1, requestUser))
                .isEqualTo(new ApplicationException(USER_ILLEGAL_STATE, null));
    }

    @Test
    void whenValidateUserAmendValidUser_thenNoExceptionThrwon() {
        requestUser.setRole(ADMIN);
        oldUser.setRole(OPERAT);
        UserDTO newUser1 = new UserDTO();
        newUser1.setRole(OPERAT);
        newUser1.setUsername("username");
        newUser1.setEmail("username@gmail.com");

        assertThatNoException().isThrownBy(() -> userValidator.validateUserAmend(oldUser, newUser1, requestUser));
    }

    @Test
    void whenValidateUserCreateInactvUser_thenThrowInactvUserCreate() {
        requestUser.setRole(ADMIN);
        newUser.setEmail("username@gmail.com");
        newUser.setStatus(INACTV);
        newUser.setRole(OPERAT);

        assertThatThrownBy(() -> userValidator.validateUserCreate(newUser, requestUser))
                .isEqualTo(new ApplicationException(INACTIV_USER_CREATE, newUser.getUsername()));
    }

    @Test
    void whenValidateUserCreateInvalidUser_thenThrowUserMissingProperty() {
        requestUser.setRole(ADMIN);
        newUser.setStatus(SUSPND);
        newUser.setRole(OPERAT);

        assertThatThrownBy(() -> userValidator.validateUserCreate(newUser, requestUser))
                .isEqualTo(new ApplicationException(USER_MISSING_PROPERTY, null));
    }

    @Test
    void whenValidateUserCreateValidUser_thenNoExceptionThrown() {
        requestUser.setRole(ADMIN);
        newUser.setEmail("username@gmail.com");
        newUser.setStatus(ACTIVE);
        newUser.setRole(OPERAT);

        assertThatNoException().isThrownBy(() -> userValidator.validateUserCreate(newUser, requestUser));
    }

}
