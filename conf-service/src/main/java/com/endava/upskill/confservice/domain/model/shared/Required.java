package com.endava.upskill.confservice.domain.model.shared;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;

import com.endava.upskill.confservice.domain.model.exception.ExceptionResponse;

@NotNull
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
public @interface Required {

    String message() default "${exceptionResponse.messageTemplate}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    ExceptionResponse exceptionResponse();
}
