package com.endava.upskill.confservice.domain.model.user;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {StatusActiveOrSuspnd.StatusActiveOrSuspndValidator.class})
public @interface StatusActiveOrSuspnd {

    String message() default "${exceptionResponse.messageTemplate}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    ExceptionResponse exceptionResponse() default ExceptionResponse.USER_VALIDATION_STATUS;

    class StatusActiveOrSuspndValidator implements ConstraintValidator<StatusActiveOrSuspnd, Status> {

        @Override
        public boolean isValid(Status value, ConstraintValidatorContext context) {
            return Objects.nonNull(value) && value != Status.INACTV;
        }
    }
}
