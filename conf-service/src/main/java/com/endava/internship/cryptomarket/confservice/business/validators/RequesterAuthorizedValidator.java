package com.endava.internship.cryptomarket.confservice.business.validators;

import com.endava.internship.cryptomarket.confservice.business.UserService;
import com.endava.internship.cryptomarket.confservice.business.validators.annotations.RequesterAuthorized;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.CLIENT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.INACTV;

@Component
@RequiredArgsConstructor
public class RequesterAuthorizedValidator implements ConstraintValidator<RequesterAuthorized, Optional<User>> {

    private final UserService userService;

    @Override
    public boolean isValid(final Optional<User> optionalUser, final ConstraintValidatorContext constraintValidatorContext) {
        if(optionalUser.isEmpty()){
            return true;
        }

        final User user = optionalUser.get();

        if (!userService.userExists(user) || user.getRole() == CLIENT) {
            return false;
        }

        return user.getStatus() != INACTV;
    }
}
