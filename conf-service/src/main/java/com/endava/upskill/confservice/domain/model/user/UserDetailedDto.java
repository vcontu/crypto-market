package com.endava.upskill.confservice.domain.model.user;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public record UserDetailedDto(
        String username,
        String email,
        Status status,
        LocalDateTime createdOn,
        LocalDateTime updatedOn,
        String updatedBy) {

    public static UserDetailedDto fromUser(User user) {
        return new UserDetailedDto(user.getUsername(), user.getEmail(), user.getStatus(),
                user.getCreatedOn(), user.getUpdatedOn(), user.getUpdatedBy());
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static class Fields {

        public static final String username = "username";

        public static final String email = "email";

        public static final String status = "status";

        public static final String createdOn = "createdOn";

        public static final String updatedOn = "updatedOn";

        public static final String updatedBy = "updatedBy";

    }
}
