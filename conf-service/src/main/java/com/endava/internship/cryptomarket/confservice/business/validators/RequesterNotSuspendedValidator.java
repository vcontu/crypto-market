package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.validators.annotations.RequesterNotSuspended;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.endava.internship.cryptomarket.confservice.data.model.Status.SUSPND;

@Component
public class RequesterNotSuspendedValidator implements ConstraintValidator<RequesterNotSuspended, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        return user.getStatus() != SUSPND;
    }
}
