package com.endava.upskill.confservice.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.endava.upskill.confservice.domain.dao.UserRepository;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.user.User;
import com.endava.upskill.confservice.domain.model.user.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.user.UserDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.getAll();
        return users.stream().map(UserDto::fromUser).toList();
    }

    @Override
    public UserDetailedDto getUser(String username) {
        final User user = userRepository.get(username).orElseThrow(() -> DomainException.ofUserNotFound(username));
        return UserDetailedDto.fromUser(user);
    }

    @Override
    public void createUser(UserDto userDto, LocalDateTime createdOn, String requestUsername) {
        final User user = User.from(userDto, createdOn, requestUsername); //requester is already validated in filter
        final boolean userAlreadyExists = !userRepository.save(user);
        if (userAlreadyExists) {
            throw DomainException.ofUserAlreadyExists(userDto.username());
        }
    }

    @Override
    public void deleteUser(String username, String requesterUsername) {
        boolean nothingToDelete = !userRepository.delete(username);
        if (nothingToDelete) {
            throw DomainException.ofUserNotFound(username);
        }
    }

    @Override
    public Optional<User> getRequester(String username) {
        return userRepository.get(username);
    }
}
