package com.endava.upskill.confservice.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.endava.upskill.confservice.domain.model.user.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.endava.upskill.confservice.domain.model.user.User;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDetailedDto getUser(String username);

    void createUser(UserDto user, LocalDateTime now, String requestUser);

    void deleteUser(String username, String requesterUsername);

    Optional<User> getRequester(String username);
}
