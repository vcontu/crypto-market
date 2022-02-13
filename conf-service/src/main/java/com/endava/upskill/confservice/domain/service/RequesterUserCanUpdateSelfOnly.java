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

import com.endava.upskill.confservice.application.AdminConfig;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;

import lombok.extern.slf4j.Slf4j;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Constraint(validatedBy = {RequesterUserCanUpdateSelfOnly.RequesterUserCanUpdateSelfOnlyValidator.class})
public @interface RequesterUserCanUpdateSelfOnly {

    String message() default "${formatter.format(exceptionResponse.messageTemplate, validatedValue[0])}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    ExceptionResponse exceptionResponse() default ExceptionResponse.REQUESTER_UPDATE_SELF;

    @SupportedValidationTarget(ValidationTarget.PARAMETERS)
    @Slf4j
    class RequesterUserCanUpdateSelfOnlyValidator implements ConstraintValidator<RequesterUserCanUpdateSelfOnly, Object[]> {

        private static final int REQUESTER_PARAM_INDEX = 0;

        private static final int USERNAME_PARAM_INDEX = 1;

        @Override
        public boolean isValid(Object[] value, ConstraintValidatorContext context) {
            if (value[REQUESTER_PARAM_INDEX] instanceof String requester &&
                value[USERNAME_PARAM_INDEX] instanceof String username) {

                if (AdminConfig.ADMIN_USERNAME.equals(requester)) {
                    return true;
                }

                return requester.equals(username);
            } else {
                final String constraintName = RequesterUserCanUpdateSelfOnly.class.getSimpleName();
                log.error("Constraint {} placed on wrong method", constraintName);
                throw new ConstraintDeclarationException("Constraint %s placed on wrong parameters".formatted(constraintName));
            }
        }
    }
}
