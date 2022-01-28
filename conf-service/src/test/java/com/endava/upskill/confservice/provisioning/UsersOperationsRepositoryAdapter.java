package com.endava.upskill.confservice.provisioning;

import java.time.LocalDateTime;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.endava.upskill.confservice.domain.dao.UserRepository;
import com.endava.upskill.confservice.domain.model.user.User;
import com.endava.upskill.confservice.domain.model.user.UserDto;

public class UsersOperationsRepositoryAdapter implements UserOperations {

    @Override
    public void createUser(UserDto userDto, LocalDateTime localDateTime, String requestUser, ExtensionContext extensionContext) {
        final User user = User.from(userDto, localDateTime, requestUser);
        getUserRepository(extensionContext).save(user);
    }

    @Override
    public void deleteUser(String username, String requesterUsername, ExtensionContext extensionContext) {
        getUserRepository(extensionContext).delete(username);
    }

    private UserRepository getUserRepository (ExtensionContext extensionContext) {
        return SpringExtension.getApplicationContext(extensionContext).getBean(UserRepository.class);
    }
}
