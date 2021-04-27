package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.UserService;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.stream.Stream;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.CLIENT;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.INACTV;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequesterAuthorizedValidatorTest {

    @Mock
    ConstraintValidatorContext context;

    @Mock
    UserService userService;

    @InjectMocks
    private RequesterAuthorizedValidator validator;

    private static Stream<Arguments> amendTests() {
        final String username = "user";
        return Stream.of(
                Arguments.of(User.builder().username(username).email("user@gmail.com").role(CLIENT).status(ACTIVE).build(), false, false),
                Arguments.of(User.builder().username(username).email("user@gmail.com").role(CLIENT).status(ACTIVE).build(), true, false),
                Arguments.of(User.builder().username(username).email("user@gmail.com").role(OPERAT).status(INACTV).build(), true, false),
                Arguments.of(User.builder().username(username).email("user@gmail.com").role(OPERAT).status(ACTIVE).build(), true, true)
        );
    }

    @ParameterizedTest
    @MethodSource("amendTests")
    void whenCheckIfValid_thenValidate(User user, boolean exists, boolean response) {
        when(userService.userExists(user)).thenReturn(exists);
        assertThat(validator.isValid(Optional.of(user), context)).isEqualTo(response);
    }

    @Test
    void whenCheckIfNullIsValid_thenReturnFalse() {
        assertThat(validator.isValid(Optional.empty(), context)).isTrue();
    }


}
