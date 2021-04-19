package com.endava.internship.cryptomarket.confservice.data.model;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static java.util.Objects.nonNull;
import static java.util.regex.Pattern.matches;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "username")
public class User {

    private final String username;

    private String email;

    private Roles role;

    private Status status;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String updatedBy;

    User(String username, String email, Roles role, Status status,
         LocalDateTime createdOn, LocalDateTime updatedOn, String updatedBy) {
        if (nonNull(email) && !matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", email)) {
            throw new ApplicationException(ExceptionResponses.USER_ILLEGAL_STATE, null);
        }
        if (!matches("^(?=.{4,32}$)(?![_])(?!.*[_]{2})[a-z0-9_]+(?<![_])$", username)) {
            throw new ApplicationException(ExceptionResponses.USER_ILLEGAL_STATE, null);
        }
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
    }

    public static User of(UserDTO userDTO) {
        return User.builder().username(userDTO.getUsername()).email(userDTO.getEmail())
                .role(userDTO.getRole()).status(userDTO.getStatus()).build();
    }
}
