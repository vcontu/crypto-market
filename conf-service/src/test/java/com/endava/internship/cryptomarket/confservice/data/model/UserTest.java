package com.endava.internship.cryptomarket.confservice.data.model;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import org.junit.jupiter.api.Test;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.ADMIN;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.SUSPND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void whenCreateUserWithInvalidUsername_thenThrowUserIllegalState() {
        assertThatThrownBy(() -> new User("Invalid__Use%$ame", "gmail@gmail.com",
                ADMIN, SUSPND, null, null, null))
                .isEqualTo(new ApplicationException(ExceptionResponses.USER_ILLEGAL_STATE, null));
    }

    @Test
    void whenCreateUserWithInvalidEmail_thenThrowUserIllegalState() {
        assertThatThrownBy(() -> new User("valid_username", "invalidEmail",
                ADMIN, SUSPND, null, null, null))
                .isEqualTo(new ApplicationException(ExceptionResponses.USER_ILLEGAL_STATE, null));
    }

}
