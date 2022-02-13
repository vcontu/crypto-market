package com.endava.upskill.confservice.domain.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.util.Objects.isNull;

import javax.validation.Constraint;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import com.endava.upskill.confservice.application.AdminConfig;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;
import com.endava.upskill.confservice.domain.model.update.UserUpdateDto;

import lombok.extern.slf4j.Slf4j;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Constraint(validatedBy = {RequesterUserCannotUpdateStatus.RequesterUserCannotUpdateStatusValidator.class})
public @interface RequesterUserCannotUpdateStatus {

    String message() default "${formatter.format(exceptionResponse.messageTemplate, validatedValue[0])}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    ExceptionResponse exceptionResponse() default ExceptionResponse.REQUESTER_UPDATE_STATUS;

    @SupportedValidationTarget(ValidationTarget.PARAMETERS)
    @Slf4j
    class RequesterUserCannotUpdateStatusValidator implements ConstraintValidator<RequesterUserCannotUpdateStatus, Object[]> {

        private static final int REQUESTER_PARAM_INDEX = 0;

        private static final int DTO_PARAM_INDEX = 2;

        @Override
        public boolean isValid(Object[] value, ConstraintValidatorContext context) {
            if (value[REQUESTER_PARAM_INDEX] instanceof String requester &&
                value[DTO_PARAM_INDEX] instanceof UserUpdateDto updateDto) {

                if (AdminConfig.ADMIN_USERNAME.equals(requester)) {
                    return true;
                }

                return isNull(updateDto.status());
            } else {
                final String constraintName = RequesterUserCannotUpdateStatusValidator.class.getSimpleName();
                log.error("Constraint {} placed on wrong method", constraintName);
                throw new ConstraintDeclarationException("Constraint %s placed on wrong parameters".formatted(constraintName));
            }
        }
    }
}
