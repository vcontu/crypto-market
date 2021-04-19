package com.endava.internship.cryptomarket.confservice.business;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ApplicationException;
import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import com.endava.internship.cryptomarket.confservice.business.validator.UserValidator;
import com.endava.internship.cryptomarket.confservice.data.UserRepository;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.*;
import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserValidator userValidator;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.getAll();
        return users.stream().map(UserDTO::of).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUser(String username) {
        User user = userRepository.get(username)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND, username));
        return UserDTO.ofDetailedUser(user);
    }

    @Override
    public void createUser(UserDTO newUser, LocalDateTime now, String requestUsername) {
        User requestUser = userRepository.get(requestUsername).get();

        userValidator.validateUserCreate(newUser, requestUser);
        if (userRepository.get(newUser.getUsername()).isPresent()) {
            throw new ApplicationException(USER_ALREADY_EXISTS, newUser.getUsername());
        }

        User user = User.of(newUser);
        user.setCreatedOn(now);
        boolean done = userRepository.save(user);

    }

    @Override
    public void amendUser(String username, UserDTO newUser, String requestUsername, LocalDateTime now) {
        User requestUser = userRepository.get(requestUsername).get();

        User oldUser = userRepository.get(username)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND, username));
        userValidator.validateUserAmend(oldUser, newUser, requestUser);


        modify(oldUser, newUser, requestUsername, now);
    }

    private void modify(User user, UserDTO newUser, String requestUser, LocalDateTime now) {
        boolean hasChanged = false;
        if (nonNull(newUser.getEmail()) && !user.getEmail().equals(newUser.getEmail())) {
            user.setEmail(newUser.getEmail());
            hasChanged = true;
        }
        if (nonNull(newUser.getRole()) && !user.getRole().equals(newUser.getRole())) {
            user.setRole(newUser.getRole());
            hasChanged = true;
        }
        if (nonNull(newUser.getStatus()) && !user.getStatus().equals(newUser.getStatus())) {
            user.setStatus(newUser.getStatus());
            hasChanged = true;
        }
        if (hasChanged) {
            user.setUpdatedBy(requestUser);
            user.setUpdatedOn(now);
        } else {
            throw new ApplicationException(USER_NOT_CHANGED, null);
        }
    }

    @Override
    public void deleteUser(String username) {
        boolean done = userRepository.delete(username);

        if (!done) {
            throw new ApplicationException(USER_NOT_FOUND, username);
        }
    }

    @Override
    public Optional<User> getRequester(String username) {
        return userRepository.get(username);
    }

}
