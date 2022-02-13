package com.endava.upskill.confservice.domain.model.update;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.util.Objects.nonNull;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = {MinimumOnePropertyUpdated.MinimumOnePropertyUpdatedValidator.class})
public @interface MinimumOnePropertyUpdated {

    String message() default "${exceptionResponse.messageTemplate}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    ExceptionResponse exceptionResponse() default ExceptionResponse.USER_UPDATE_NO_PROPERTIES;

    class MinimumOnePropertyUpdatedValidator implements ConstraintValidator<MinimumOnePropertyUpdated, UserUpdateDto> {

        @Override
        public boolean isValid(UserUpdateDto userDto, ConstraintValidatorContext context) {
            return nonNull(userDto.email()) || nonNull(userDto.status());
        }
    }
}
