package com.endava.upskill.confservice.api.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.endava.upskill.confservice.application.AdminConfig;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;

import lombok.extern.slf4j.Slf4j;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Inherited
@Documented
@Constraint(validatedBy = AdminOnly.NotAdminValidator.class)
public @interface AdminOnly {

    String message() default "${formatter.format(exceptionResponse.messageTemplate, validatedValue)}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    ExceptionResponse exceptionResponse() default ExceptionResponse.AUTHORIZATION_FAILED;

    @Slf4j
    class NotAdminValidator implements ConstraintValidator<AdminOnly, String> {

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (!AdminConfig.ADMIN_USERNAME.equals(value)) {
                log.warn("Authorization failure for requester: {}", value);
                return false;
            }
            return true;
        }
    }
}
