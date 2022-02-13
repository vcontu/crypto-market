package com.endava.upskill.confservice.domain.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.endava.upskill.confservice.domain.model.update.UserUpdateDto;

import lombok.extern.slf4j.Slf4j;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Constraint(validatedBy = {PathUsernameEqualsBodyUsername.PathUsernameEqualsBodyUsernameValidator.class})
public @interface PathUsernameEqualsBodyUsername {

    String message() default "${exceptionResponse.messageTemplate}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    ExceptionResponse exceptionResponse() default ExceptionResponse.USERNAME_DIFFERENT;

    @SupportedValidationTarget(ValidationTarget.PARAMETERS)
    @Slf4j
    class PathUsernameEqualsBodyUsernameValidator implements ConstraintValidator<PathUsernameEqualsBodyUsername,Object[]> {

        private static final int USERNAME_PARAM_INDEX = 1;

        private static final int DTO_PARAM_INDEX = 2;

        @Override
        public boolean isValid(Object[] value, ConstraintValidatorContext context) {
            if (value[DTO_PARAM_INDEX] instanceof UserUpdateDto userUpdateDto &&
                value[USERNAME_PARAM_INDEX] instanceof String username) {
                return username.equals(userUpdateDto.username());
            } else {
                final String constraintName = PathUsernameEqualsBodyUsername.class.getSimpleName();
                log.error("Constraint {} placed on wrong method", constraintName);
                throw new ConstraintDeclarationException("Constraint %s placed on wrong parameters".formatted(constraintName));
            }
        }
    }
}
