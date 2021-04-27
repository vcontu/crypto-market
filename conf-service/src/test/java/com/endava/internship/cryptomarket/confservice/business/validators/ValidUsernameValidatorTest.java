package com.endava.internship.cryptomarket.confservice.business.validators;

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
class ValidUsernameValidatorTest {

    @Mock
    ConstraintValidatorContext context;

    private ValidUsernameValidator validator;

    private static Stream<Arguments> amendTests() {
        return Stream.of(
                Arguments.of("usr", false),
                Arguments.of("user", true)
        );
    }

    @BeforeEach
    void setUp() {
        validator = new ValidUsernameValidator();
    }

    @ParameterizedTest
    @MethodSource("amendTests")
    void whenCheckIfValid_thenValidate(String username, boolean response) {
        assertThat(validator.isValid(username, context)).isEqualTo(response);
    }

}
