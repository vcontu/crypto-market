package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import com.endava.internship.cryptomarket.confservice.business.validators.annotations.ValidCreateRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidCreateRequestValidator implements ConstraintValidator<ValidCreateRequest, UserDto> {

    @Override
    public boolean isValid(final UserDto user, final ConstraintValidatorContext constraintValidatorContext) {
        return user.validForCreateRequest();
    }
}
