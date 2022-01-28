package com.endava.upskill.confservice.domain.model.user;

import javax.validation.GroupSequence;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@GroupSequence({
        UsernamePattern.class,
        ValidEmail.class,
        StatusActiveOrSuspnd.class,
        UserDto.class})
public record UserDto(
        @UsernamePattern(groups = UsernamePattern.class)
        String username,

        @ValidEmail(groups = ValidEmail.class)
        String email,

        @StatusActiveOrSuspnd(groups = StatusActiveOrSuspnd.class)
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
