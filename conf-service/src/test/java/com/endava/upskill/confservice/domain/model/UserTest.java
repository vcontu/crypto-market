package com.endava.upskill.confservice.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.endava.upskill.confservice.domain.model.user.Status;
import com.endava.upskill.confservice.domain.model.user.User;
import com.endava.upskill.confservice.domain.model.exception.DomainException;

class UserTest {

    @Test
    void whenValidUser_thenDontThrowExceptions() {
        Assertions.assertThatNoException().isThrownBy(
                () -> new User("validuser", "username@gmail.com",
                        Status.ACTIVE, null, null, null));
    }

    @Test
    void whenInvalidUsername_thenUsernameValidationFailure() {
        assertThatThrownBy(() -> new User("Invalid__Use%$ame", "username@gmail.com",
                Status.ACTIVE, null, null, null))
                .isEqualTo(DomainException.ofUserValidationUsername());
    }

    @Test
    void whenInvalidEmail_thenEmailValidationFailure() {
        assertThatThrownBy(() -> new User("validuser", "invalidEmail",
                Status.ACTIVE, null, null, null))
                .isEqualTo(DomainException.ofUserValidationEmail());
    }
}
