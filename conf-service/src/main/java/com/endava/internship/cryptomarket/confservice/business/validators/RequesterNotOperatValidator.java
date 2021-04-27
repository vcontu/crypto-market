package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.validators.annotations.RequesterNotOperat;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;

@Component
public class RequesterNotOperatValidator implements ConstraintValidator<RequesterNotOperat, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        return user.getRole() != OPERAT;
    }
}
