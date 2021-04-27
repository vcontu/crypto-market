package com.endava.internship.cryptomarket.confservice.data.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "username")
@AllArgsConstructor
public class User {

    private final String username;

    private String email;

    private Roles role;

    private Status status;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String updatedBy;

}
