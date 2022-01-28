package com.endava.upskill.confservice.domain.service;

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

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Inherited
@Documented
@Constraint(validatedBy = NotAdmin.NotAdminValidator.class)
public @interface NotAdmin {

    String message() default "${formatter.format(exceptionResponse.messageTemplate, validatedValue)}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    ExceptionResponse exceptionResponse() default ExceptionResponse.USER_NOT_REMOVABLE;

    class NotAdminValidator implements ConstraintValidator<NotAdmin, String> {

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !AdminConfig.ADMIN_USERNAME.equals(value);
        }
    }
}
