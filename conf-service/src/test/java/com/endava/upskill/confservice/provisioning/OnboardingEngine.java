package com.endava.upskill.confservice.provisioning;

import java.time.LocalDateTime;

import org.junit.jupiter.api.extension.ExtensionContext;

import com.endava.upskill.confservice.domain.model.create.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public enum OnboardingEngine implements UserOperations {

    REST(new UserOperationsRestAdapter()),
    REPOSITORY(new UsersOperationsRepositoryAdapter());

    private final UserOperations userOperations;

    @Override
    public void createUser(UserDto user, LocalDateTime localDateTime, String requestUser, ExtensionContext extensionContext) {
        log.info("[TEST] On-boarding user: {}; by requester: {}; engine: {}", user.username(), requestUser, this);
        userOperations.createUser(user, localDateTime, requestUser, extensionContext);
    }

    @Override
    public void deleteUser(String username, String requesterUsername, ExtensionContext extensionContext) {
        log.info("[TEST] Off-boarding user: {}; by requester: {}; engine: {}", username, requesterUsername, this);
        userOperations.deleteUser(username, requesterUsername, extensionContext);
    }
}
