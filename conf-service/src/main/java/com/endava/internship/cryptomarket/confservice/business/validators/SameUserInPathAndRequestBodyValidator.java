package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import com.endava.internship.cryptomarket.confservice.business.validators.annotations.SameUserInPathAndRequestBody;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class SameUserInPathAndRequestBodyValidator implements ConstraintValidator<SameUserInPathAndRequestBody, Object[]> {

    public static final int USERNAME_PARAMETER = 0;
    public static final int USER_PARAMETER = 1;

    @Override
    public boolean isValid(Object[] objects, ConstraintValidatorContext constraintValidatorContext) {
        String username = (String) objects[USERNAME_PARAMETER];
        UserDto user = (UserDto) objects[USER_PARAMETER];
        return username.equals(user.getUsername());
    }
}
