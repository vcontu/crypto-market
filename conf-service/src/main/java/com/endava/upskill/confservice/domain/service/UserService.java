package com.endava.upskill.confservice.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.endava.upskill.confservice.domain.model.user.User;
import com.endava.upskill.confservice.domain.model.user.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.user.UserDto;

@Validated(Priorities.class)
public interface UserService {

    List<UserDto> getAllUsers();

    UserDetailedDto getUser(String username);

    void createUser(@Valid UserDto user, LocalDateTime now, String requestUser);

    void deleteUser(@NotAdmin(groups = Priorities.P1.class) String username, String requesterUsername);

    Optional<User> getRequester(String username);
}
