package com.endava.internship.cryptomarket.confservice.data.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import static javax.persistence.EnumType.STRING;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "username")
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor
@ToString
@Entity(name = "T_USER")
public class User {

    @Id
    private String username;

    private String email;

    @Enumerated(STRING)
    private Roles role;

    @Enumerated(STRING)
    private Status status;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @Column(name = "updated_by")
    private String updatedBy;

}
