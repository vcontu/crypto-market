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
class ValidEmailValidatorTest {

    @Mock
    ConstraintValidatorContext context;

    private ValidEmailValidator validator;

    private static Stream<Arguments> amendTests() {
        return Stream.of(
                Arguments.of("gmail.com", false),
                Arguments.of("user@gmail.com", true)
        );
    }

    @BeforeEach
    void setUp() {
        validator = new ValidEmailValidator();
    }

    @ParameterizedTest
    @MethodSource("amendTests")
    void whenCheckIfValid_thenValidate(String email, boolean response) {
        assertThat(validator.isValid(email, context)).isEqualTo(response);
    }

}
