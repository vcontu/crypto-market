package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import com.endava.internship.cryptomarket.confservice.business.validators.annotations.AdminCannotCreateAdmin;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.ADMIN;

@Component
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class AdminCannotCreateAdminValidator implements ConstraintValidator<AdminCannotCreateAdmin, Object[]> {

    public static final int USER_PARAMETER = 0;
    public static final int REQUESTER_USER_PARAMETER = 1;

    @Override
    public boolean isValid(Object[] objects, ConstraintValidatorContext constraintValidatorContext) {
        UserDto user = (UserDto) objects[USER_PARAMETER];
        User requester = (User) objects[REQUESTER_USER_PARAMETER];
        return (requester.getRole() != ADMIN || user.getRole() != ADMIN);
    }
}
