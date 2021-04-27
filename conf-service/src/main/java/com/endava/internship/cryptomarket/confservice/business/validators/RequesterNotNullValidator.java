package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.validators.annotations.RequesterNotNull;
import com.endava.internship.cryptomarket.confservice.data.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class RequesterNotNullValidator implements ConstraintValidator<RequesterNotNull, Optional<User>> {

    @Override
    public boolean isValid(final Optional<User> user, final ConstraintValidatorContext constraintValidatorContext) {
        return user.isPresent();
    }
}
