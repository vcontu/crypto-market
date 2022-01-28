package com.endava.upskill.confservice.provisioning;

import java.time.LocalDateTime;

import org.junit.jupiter.api.extension.ExtensionContext;

import com.endava.upskill.confservice.domain.model.user.UserDto;

public interface UserOperations {

    void createUser(UserDto user, LocalDateTime localDateTime, String requestUser, ExtensionContext extensionContext);

    void deleteUser(String username, String requesterUsername, ExtensionContext extensionContext);
}
