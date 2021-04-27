package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SameUserInPathAndRequestBodyValidatorTest {

    private SameUserInPathAndRequestBodyValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new SameUserInPathAndRequestBodyValidator();
    }

    private static Stream<Arguments> amendTests() {
        return Stream.of(
                Arguments.of("user", "user", true),
                Arguments.of("notUser", "user", false)
        );
    }

    @ParameterizedTest
    @MethodSource("amendTests")
    void whenCheckIfValid_thenValidate(String entityUsername, String userUsername, boolean response) {
        UserDto entity = new UserDto(entityUsername, null, null,
                null, null, null, null);

        Object[] arguments = new Object[]{userUsername, entity};

        assertThat(validator.isValid(arguments, context)).isEqualTo(response);
    }

}
