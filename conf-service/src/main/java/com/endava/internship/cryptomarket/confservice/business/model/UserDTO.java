package com.endava.internship.cryptomarket.confservice.business.model;

import com.endava.internship.cryptomarket.confservice.data.model.Roles;
import com.endava.internship.cryptomarket.confservice.data.model.Status;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private String username;

    private String email;

    private Roles role;

    private Status status;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String updatedBy;

    public static UserDTO of(User user) {
        return new UserDTO(user.getUsername(), user.getEmail(),
                user.getRole(), user.getStatus(), null, null, null);
    }

    public static UserDTO ofDetailedUser(User user) {
        return new UserDTO(user.getUsername(), user.getEmail(), user.getRole(), user.getStatus(),
                user.getCreatedOn(), user.getUpdatedOn(), user.getUpdatedBy());
    }

}
