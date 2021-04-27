package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.SUSPND;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RequesterNotSuspendedValidatorTest {

    @Mock
    ConstraintValidatorContext context;

    private RequesterNotSuspendedValidator validator;

    private static Stream<Arguments> amendTests() {
        final String username = "user";
        return Stream.of(
                Arguments.of(User.builder().username(username).email("user@gmail.com").role(OPERAT).status(SUSPND).build(), false),
                Arguments.of(User.builder().username(username).email("user@gmail.com").role(OPERAT).status(ACTIVE).build(), true)
        );
    }

    @BeforeEach
    void setUp() {
        validator = new RequesterNotSuspendedValidator();
    }

    @ParameterizedTest
    @MethodSource("amendTests")
    void whenCheckIfValid_thenValidate(User user, boolean response) {
        assertThat(validator.isValid(user, context)).isEqualTo(response);
    }

}
