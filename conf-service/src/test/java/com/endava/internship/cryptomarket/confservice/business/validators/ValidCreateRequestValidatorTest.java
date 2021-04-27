package com.endava.internship.cryptomarket.confservice.business.validator;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import com.endava.internship.cryptomarket.confservice.business.validators.ValidCreateRequestValidator;
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
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ValidCreateRequestValidatorTest {

    @Mock
    ConstraintValidatorContext context;

    private ValidCreateRequestValidator validator;

    private static Stream<Arguments> amendTests() {
        return Stream.of(
                Arguments.of(new UserDto(null, "user@gmail.com", OPERAT,
                        ACTIVE, null, null, null), false),
                Arguments.of(new UserDto("user", null, null,
                        null, null, null, null), false),
                Arguments.of(new UserDto("user", null, OPERAT,
                        ACTIVE, null, null, null), false),
                Arguments.of(new UserDto("user", "user@gmail.com", null,
                        ACTIVE, null, null, null), false),
                Arguments.of(new UserDto("user", "user@gmail.com", OPERAT,
                        null, null, null, null), false),
                Arguments.of(new UserDto("user", "user@gmail.com", OPERAT,
                        ACTIVE, null, null, null), true)
        );
    }

    @BeforeEach
    void setUp() {
        validator = new ValidCreateRequestValidator();
    }

    @ParameterizedTest
    @MethodSource("amendTests")
    void whenCheckIfValid_thenValidate(UserDto userDTO, boolean response) {
        assertThat(validator.isValid(userDTO, context)).isEqualTo(response);
    }

}
