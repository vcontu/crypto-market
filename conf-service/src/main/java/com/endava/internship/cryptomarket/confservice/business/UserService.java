package com.endava.internship.cryptomarket.confservice.business;

import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import com.endava.internship.cryptomarket.confservice.data.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUser(String username);

    void createUser(UserDTO user, LocalDateTime now, String requestUser);

    void amendUser(String username, UserDTO newUser, String requestUser, LocalDateTime now);

    void deleteUser(String username);

    Optional<User> getRequester(String username);

}
