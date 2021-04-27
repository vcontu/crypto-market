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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RequesterCannotSelfAmendValidatorTest {

    private RequesterCannotSelfAmendValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    private static Stream<Arguments> amendTests() {
        return Stream.of(
                Arguments.of("user", "user", false),
                Arguments.of("requester", "user", true)
        );
    }

    @BeforeEach
    void setUp() {
        validator = new RequesterCannotSelfAmendValidator();
    }

    @ParameterizedTest
    @MethodSource("amendTests")
    void whenCheckIfValid_thenValidate(String requesterUsername, String userUsername, boolean response) {
        User requester = User.builder().username(requesterUsername).build();


        Object[] arguments = new Object[]{userUsername, null, requester};

        assertThat(validator.isValid(arguments, context)).isEqualTo(response);
    }

}
