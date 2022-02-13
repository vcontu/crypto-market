package com.endava.upskill.confservice.domain.dao;

import java.util.List;
import java.util.Optional;

import com.endava.upskill.confservice.domain.model.entity.User;

public interface UserRepository {

    List<User> getAll();

    Optional<User> get(String username);

    boolean save(User newUser);

    boolean delete(String username);

    boolean update(User updatingUser);
}
