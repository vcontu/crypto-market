package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.UserService;
import com.endava.internship.cryptomarket.confservice.business.validators.annotations.AmendedUserNotInactv;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.endava.internship.cryptomarket.confservice.data.model.Status.INACTV;

@Component
@RequiredArgsConstructor
public class AmendedUserNotInactvValidator implements ConstraintValidator<AmendedUserNotInactv, String> {

    private final UserService userService;

    @Override
    public boolean isValid(final String username, final ConstraintValidatorContext constraintValidatorContext) {
        User user = userService.getRequesterUser(username).get();

        return user.getStatus() != INACTV;
    }
}
