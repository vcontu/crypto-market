package com.endava.internship.cryptomarket.confservice.business.validators.annotations;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import com.endava.internship.cryptomarket.confservice.business.validators.RequesterCannotSelfAmendValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.SELF_AMEND;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Constraint(validatedBy = RequesterCannotSelfAmendValidator.class)
public @interface RequesterCannotSelfAmend {

    ExceptionResponses response() default SELF_AMEND;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
