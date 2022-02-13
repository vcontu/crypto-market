package com.endava.upskill.confservice.domain.model.create;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;

@Pattern(regexp = "^(?=.{5,32}$)(?![0-9])[a-z0-9]+$")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
public @interface UsernamePattern {

    String message() default "${exceptionResponse.messageTemplate}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    ExceptionResponse exceptionResponse() default ExceptionResponse.USER_VALIDATION_USERNAME;
}
