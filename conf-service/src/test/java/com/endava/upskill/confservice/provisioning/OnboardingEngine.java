package com.endava.upskill.confservice.provisioning;

import java.time.LocalDateTime;

import org.junit.jupiter.api.extension.ExtensionContext;

import com.endava.upskill.confservice.domain.model.user.UserDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OnboardingEngine implements UserOperations {

    REST(new UserOperationsRestAdapter());

    private final UserOperations userOperations;

    @Override
    public void createUser(UserDto user, LocalDateTime localDateTime, String requestUser, ExtensionContext extensionContext) {
        userOperations.createUser(user, localDateTime, requestUser, extensionContext);
    }

    @Override
    public void deleteUser(String username, String requesterUsername, ExtensionContext extensionContext) {
        userOperations.deleteUser(username, requesterUsername, extensionContext);
    }
}
