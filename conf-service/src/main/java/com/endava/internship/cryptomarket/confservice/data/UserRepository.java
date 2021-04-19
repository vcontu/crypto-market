package com.endava.internship.cryptomarket.confservice.data;

import com.endava.internship.cryptomarket.confservice.data.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> getAll();

    Optional<User> get(String username);

    boolean save(User newUser);

    boolean delete(String username);


}
