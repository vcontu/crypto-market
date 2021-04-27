package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.validators.annotations.RequesterCannotSelfAmend;
import com.endava.internship.cryptomarket.confservice.data.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class RequesterCannotSelfAmendValidator implements ConstraintValidator<RequesterCannotSelfAmend, Object[]> {

    public static final int USERNAME_PARAMETER = 0;
    public static final int REQUESTER_USERNAME_PARAMETER = 2;

    @Override
    public boolean isValid(Object[] objects, ConstraintValidatorContext constraintValidatorContext) {
        String username = (String) objects[USERNAME_PARAMETER];
        String requesterUsername = ((User) objects[REQUESTER_USERNAME_PARAMETER]).getUsername();
        return !username.equals(requesterUsername);
    }
}
