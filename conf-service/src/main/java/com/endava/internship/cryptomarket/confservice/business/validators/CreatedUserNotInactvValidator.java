package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import com.endava.internship.cryptomarket.confservice.business.validators.annotations.CreatedUserNotInactv;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.endava.internship.cryptomarket.confservice.data.model.Status.INACTV;

@Component
public class CreatedUserNotInactvValidator implements ConstraintValidator<CreatedUserNotInactv, UserDto> {

    @Override
    public boolean isValid(final UserDto user, final ConstraintValidatorContext constraintValidatorContext) {
        return user.getStatus() != INACTV;
    }
}

