package com.endava.upskill.confservice.domain.model;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.as;

import com.endava.upskill.confservice.application.ValidationConfig;
import com.endava.upskill.confservice.domain.model.create.UserDto;
import com.endava.upskill.confservice.domain.model.entity.Status;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.endava.upskill.confservice.domain.model.shared.Priorities;
import com.endava.upskill.confservice.util.Tokens;

import static com.endava.upskill.confservice.domain.model.exception.ExceptionResponse.USER_VALIDATION_EMAIL;
import static com.endava.upskill.confservice.domain.model.exception.ExceptionResponse.USER_VALIDATION_STATUS;
import static com.endava.upskill.confservice.domain.model.exception.ExceptionResponse.USER_VALIDATION_USERNAME;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ValidationConfig.class})
public class UserDtoValidationTest {

    @Autowired
    private Validator validator;

    @ParameterizedTest
    @MethodSource("invalidUserDtos")
    void validateCreateUser(UserDto userDto, ExceptionResponse exceptionResponse) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto, Priorities.class);
        Assertions.assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting("constraintDescriptor")
                .extracting("attributes", as(InstanceOfAssertFactories.MAP))
                .contains(Map.entry("exceptionResponse", exceptionResponse));
    }

    public static Stream<Arguments> invalidUserDtos() {
        return Stream.of(
                Arguments.of(new UserDto(null, null, Status.ACTIVE), USER_VALIDATION_USERNAME),
                Arguments.of(new UserDto(null, Tokens.EMAIL, null), USER_VALIDATION_USERNAME),
                Arguments.of(new UserDto(Tokens.USERNAME, null, null), USER_VALIDATION_EMAIL),
                Arguments.of(new UserDto(null, null, null), USER_VALIDATION_USERNAME),
                Arguments.of(new UserDto(null, Tokens.EMAIL, Status.ACTIVE), USER_VALIDATION_USERNAME),
                Arguments.of(new UserDto(Tokens.USERNAME, null, Status.ACTIVE), USER_VALIDATION_EMAIL),
                Arguments.of(new UserDto(Tokens.USERNAME, Tokens.EMAIL, null), USER_VALIDATION_STATUS),
                Arguments.of(new UserDto(Tokens.USERNAME, Tokens.EMAIL, Status.INACTV), USER_VALIDATION_STATUS),
                Arguments.of(new UserDto("abcd", Tokens.EMAIL, Status.ACTIVE), USER_VALIDATION_USERNAME),
                Arguments.of(new UserDto("a23456789012345678901234567890123", Tokens.EMAIL, Status.ACTIVE), USER_VALIDATION_USERNAME),
                Arguments.of(new UserDto("1abcd", Tokens.EMAIL, Status.ACTIVE), USER_VALIDATION_USERNAME),
                Arguments.of(new UserDto(Tokens.USERNAME, "Non-Email", Status.ACTIVE), USER_VALIDATION_EMAIL)
        );
    }
}