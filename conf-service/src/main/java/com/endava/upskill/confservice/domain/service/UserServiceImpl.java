package com.endava.upskill.confservice.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

import org.springframework.stereotype.Component;

import com.endava.upskill.confservice.domain.dao.UserRepository;
import com.endava.upskill.confservice.domain.model.create.UserDto;
import com.endava.upskill.confservice.domain.model.entity.Status;
import com.endava.upskill.confservice.domain.model.entity.User;
import com.endava.upskill.confservice.domain.model.exception.DomainException;
import com.endava.upskill.confservice.domain.model.get.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.update.UserUpdateDto;

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
    public void createUser(String requesterUsername, UserDto userDto, LocalDateTime createdOn) {
        final User user = User.from(userDto, createdOn, requesterUsername); //requester is already validated in filter
        final boolean userAlreadyExists = !userRepository.save(user);
        if (userAlreadyExists) {
            throw DomainException.ofUserAlreadyExists(userDto.username());
        }
    }

    @Override
    public void updateUser(String requesterUsername, String username, UserUpdateDto userUpdateDto, LocalDateTime localDateTime) {
        final User user = userRepository.get(username).orElseThrow(() -> DomainException.ofUserNotFound(username));

        if (user.getStatus() == Status.INACTV) {
            throw DomainException.ofUpdatingInactvUser(username);
        }

        updateUserEntity(userUpdateDto, user, localDateTime, requesterUsername);

        if (!userRepository.update(user)) {
            throw DomainException.ofUserNotFound(username); //We do not have ACID transaction, therefore there is no guarantee at all that the user will continue to exist
        }
    }

    private void updateUserEntity(UserUpdateDto newUserDetails, User user, LocalDateTime localDateTime, String requester) {
        if (nonNull(newUserDetails.email())) {
            user.setEmail(newUserDetails.email());
        }

        if (nonNull(newUserDetails.status())) {
            user.setStatus(newUserDetails.status());
        }

        user.setUpdatedBy(requester);
        user.setUpdatedOn(localDateTime);
    }

    @Override
    public void deleteUser(String requesterUsername, String username) {
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
