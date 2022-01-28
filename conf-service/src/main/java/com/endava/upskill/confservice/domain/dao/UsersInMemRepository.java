package com.endava.upskill.confservice.domain.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.endava.upskill.confservice.domain.model.user.User;

@Component
public class UsersInMemRepository implements UserRepository {

    private final Map<String, User> userMap = new ConcurrentHashMap<>();

    @Override
    public List<User> getAll() {
        return List.copyOf(userMap.values());
    }

    @Override
    public Optional<User> get(String username) {
        return Optional.ofNullable(userMap.get(username));
    }

    @Override
    public synchronized boolean save(User user)  {
        if (userMap.containsKey(user.getUsername())) {
            return false;
        }
        userMap.put(user.getUsername(), user);
        return true;
    }

    @Override
    public synchronized boolean delete(String username) {
        if (!userMap.containsKey(username)) {
            return false;
        }

        userMap.remove(username);
        return true;
    }
}
