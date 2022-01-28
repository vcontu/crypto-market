package com.endava.upskill.confservice.domain.model.user;

public record UserDto(
        String username,
        String email,
        Status status) {

    public static UserDto fromUser(User user) {
        return new UserDto(user.getUsername(), user.getEmail(), user.getStatus());
    }

    public static class Fields {

        public static final String username = "username";

        public static final String email = "email";

        public static final String status = "status";
    }
}
