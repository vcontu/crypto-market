package com.endava.internship.cryptomarket.confservice.business.validators.annotations;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import com.endava.internship.cryptomarket.confservice.business.validators.OperatorCannotCreateOperatorOrAdminValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.OPERAT_NOT_ALLOWED_CREATE_ADMIN_OPERAT;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Constraint(validatedBy = OperatorCannotCreateOperatorOrAdminValidator.class)
public @interface OperatorCannotCreateOperatorOrAdmin {

    ExceptionResponses response() default OPERAT_NOT_ALLOWED_CREATE_ADMIN_OPERAT;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
