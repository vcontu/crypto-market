package com.endava.upskill.confservice.domain.model.create;

import com.endava.upskill.confservice.domain.model.entity.Status;
import com.endava.upskill.confservice.domain.model.entity.User;
import com.endava.upskill.confservice.domain.model.shared.EmailPattern;
import com.endava.upskill.confservice.domain.model.shared.Priorities;
import com.endava.upskill.confservice.domain.model.shared.Required;

import static com.endava.upskill.confservice.domain.model.exception.ExceptionResponse.USER_VALIDATION_EMAIL;
import static com.endava.upskill.confservice.domain.model.exception.ExceptionResponse.USER_VALIDATION_USERNAME;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public record UserDto(
        @Required(groups = Priorities.B1.class, exceptionResponse = USER_VALIDATION_USERNAME)
        @UsernamePattern(groups = Priorities.B1.class)
        String username,

        @Required(groups = Priorities.B2.class, exceptionResponse = USER_VALIDATION_EMAIL)
        @EmailPattern(groups = Priorities.B2.class)
        String email,

        @RequiredStatusActiveOrSuspnd(groups = Priorities.B3.class)
        Status status) {

    public static UserDto fromUser(User user) {
        return new UserDto(user.getUsername(), user.getEmail(), user.getStatus());
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static class Fields {

        public static final String username = "username";

        public static final String email = "email";

        public static final String status = "status";
    }
}
