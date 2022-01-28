package com.endava.upskill.confservice.domain.model.user;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

import com.endava.upskill.confservice.domain.model.exception.DomainException;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(of = "username")
public class User {

    private static final Pattern usernamePattern = Pattern.compile("^(?=.{5,32}$)(?![0-9])[a-z0-9]+$");

    private static final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    private final String username;

    private String email;

    private Status status;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String updatedBy;

    public User(String username, String email, Status status,
            LocalDateTime createdOn, LocalDateTime updatedOn, String updatedBy) {

        if (isNull(username) || !usernamePattern.matcher(username).matches()) {
            throw DomainException.ofUserValidationUsername();
        }

        if (isNull(email) || !emailPattern.matcher(email).matches()) {
            throw DomainException.ofUserValidationEmail();
        }

        if (isNull(status)) {
            throw DomainException.ofUserValidationStatus();
        }

        this.username = username;
        this.email = email;
        this.status = status;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
    }

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
