package com.endava.internship.cryptomarket.confservice.data;

import com.endava.internship.cryptomarket.confservice.data.model.Roles;
import com.endava.internship.cryptomarket.confservice.data.model.Status;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.LocalDateTime.now;

@Component
public class UsersInMemRepository implements UserRepository {

    private final Map<String, User> users;

    public UsersInMemRepository() {
        users = new ConcurrentHashMap<>();
        users.put("admin", User.builder().username("admin").role(Roles.ADMIN)
                .email("admin@gmail.com").status(Status.ACTIVE)
                .createdOn(now()).build());
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> get(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public boolean save(User newUser) {
        if (users.containsKey(newUser.getUsername())) {
            return false;
        }
        synchronized (this) {
            users.put(newUser.getUsername(), newUser);
        }
        return true;
    }

    @Override
    public boolean delete(String username) {
        if (!users.containsKey(username)) {
            return false;
        }
        synchronized (this) {
            users.remove(username);
        }
        return true;
    }

}
