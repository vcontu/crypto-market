package com.endava.internship.cryptomarket.confservice.business.validator;

import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import com.endava.internship.cryptomarket.confservice.data.model.User;

public interface UserValidator {
    void validateUserChange(User user, User requestUser);

    void validateUserAmend(User oldUser, UserDTO newUser, User requestUser);

    void validateUserCreate(UserDTO newUser, User requestUser);
}
