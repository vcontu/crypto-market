package com.endava.internship.cryptomarket.confservice.api.datahandlers;

import com.endava.internship.cryptomarket.confservice.business.UserService;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StringToUserConverter
        implements Converter<String, Optional<User>> {

    private final UserService userService;

    @Override
    public Optional<User> convert(String username) {
        Optional<User> user = userService.getRequesterUser(username);
        if(user.isEmpty()){
            user = Optional.of(User.builder().username(username).build());
        }
        return user;
    }
}