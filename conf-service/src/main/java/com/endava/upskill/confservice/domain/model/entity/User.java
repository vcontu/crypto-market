package com.endava.upskill.confservice.domain.model.entity;

import java.time.LocalDateTime;

import com.endava.upskill.confservice.domain.model.create.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@EqualsAndHashCode(of = "username")
@AllArgsConstructor
public class User {

    private String username;

    private String email;

    private Status status;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String updatedBy;

    public static User from(UserDto userDTO, LocalDateTime createdOn, String updater) {
        return User.builder()
                .username(userDTO.username())
                .email(userDTO.email())
                .status(userDTO.status())
                .createdOn(createdOn)
                .updatedOn(createdOn)
                .updatedBy(updater)
                .build();
    }
}
