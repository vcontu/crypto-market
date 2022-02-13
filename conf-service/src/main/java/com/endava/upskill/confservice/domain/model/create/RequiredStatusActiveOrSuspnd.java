package com.endava.upskill.confservice.domain.model.create;

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

import com.endava.upskill.confservice.domain.model.entity.Status;
import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {RequiredStatusActiveOrSuspnd.StatusActiveOrSuspndValidator.class})
public @interface RequiredStatusActiveOrSuspnd {

    String message() default "${exceptionResponse.messageTemplate}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    ExceptionResponse exceptionResponse() default ExceptionResponse.USER_VALIDATION_STATUS;

    class StatusActiveOrSuspndValidator implements ConstraintValidator<RequiredStatusActiveOrSuspnd, Status> {

        @Override
        public boolean isValid(Status value, ConstraintValidatorContext context) {
            return Objects.nonNull(value) && value != Status.INACTV;
        }
    }
}
