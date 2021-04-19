package com.endava.internship.cryptomarket.confservice.business.validator;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.springframework.stereotype.Component;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.*;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.ADMIN;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.INACTV;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.regex.Pattern.matches;

@Component
public class UserValidatorImpl implements UserValidator {

    @Override
    public void validateUserChange(User user, User requestUser) {
        if (ADMIN.equals(user.getRole())
                && ADMIN.equals(requestUser.getRole())) {
            throw new ApplicationException(ADMIN_NOT_ALLOWED_CHANGE_ADMIN, requestUser.getUsername());
        }

        if ((OPERAT.equals(user.getRole())
                || ADMIN.equals(user.getRole()))
                && OPERAT.equals(requestUser.getRole())) {
            throw new ApplicationException(OPERAT_NOT_ALLOWED_CHANGE_ADMIN_OPERAT, requestUser.getUsername());
        }
    }

    @Override
    public void validateUserAmend(User oldUser, UserDTO newUser, User requestUser) {
        validateUserChange(oldUser, requestUser);
        if (nonNull(oldUser.getStatus()) && oldUser.getStatus().equals(INACTV)) {
            throw new ApplicationException(INACTIV_USER_AMEND, oldUser.getUsername());
        }
        if (isNull(newUser.getUsername()) || (isNull(newUser.getEmail())
                && isNull(newUser.getRole()) && isNull(newUser.getStatus()))) {
            throw new ApplicationException(USER_NOT_CHANGED, null);
        }
        if (nonNull(newUser.getEmail()) && !matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", newUser.getEmail())) {
            throw new ApplicationException(USER_ILLEGAL_STATE, null);
        }
        if (!matches("^(?=.{4,32}$)(?![_])(?!.*[_]{2})[a-z0-9_]+(?<![_])$", newUser.getUsername())) {
            throw new ApplicationException(USER_ILLEGAL_STATE, null);
        }
    }

    @Override
    public void validateUserCreate(UserDTO newUser, User requestUser) {
        validateUserChange(User.of(newUser), requestUser);
        if (INACTV.equals(newUser.getStatus())) {
            throw new ApplicationException(INACTIV_USER_CREATE, newUser.getUsername());
        }
        if (isNull(newUser.getUsername()) || isNull(newUser.getEmail())
                || isNull(newUser.getRole()) || isNull(newUser.getStatus())) {
            throw new ApplicationException(USER_MISSING_PROPERTY, null);
        }
    }

}
