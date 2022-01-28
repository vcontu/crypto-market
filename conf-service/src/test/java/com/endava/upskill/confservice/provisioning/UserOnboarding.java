package com.endava.upskill.confservice.provisioning;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.endava.upskill.confservice.domain.model.user.Status;
import com.endava.upskill.confservice.domain.model.user.User;
import com.endava.upskill.confservice.domain.model.user.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.user.UserDto;
import com.endava.upskill.confservice.util.Tokens;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class UserOnboarding implements BeforeEachCallback, AfterEachCallback,
                                       BeforeAllCallback, AfterAllCallback {

    private static final OnboardingEngine defaultOnboardingEngine = OnboardingEngine.REST;

    private static final String defaultCreatingRequesterUsername = Tokens.USERNAME_ADMIN;

    private static final String defaultDeletingRequesterUsername = Tokens.USERNAME_ADMIN;

    private static final ProvisioningMode defaultProvisioningMode = ProvisioningMode.ALL;

    private static final LifeCycle defaultLifeCycle = LifeCycle.PER_METHOD;

    private static final LocalDateTime defaultCreatedOn = Tokens.LDT;

    private final OnboardingEngine onboardingEngine;

    @Getter
    private final UserDto userDto;

    @Getter
    private final User user;

    private final String createdBy;

    private final ProvisioningMode provisioningMode;

    private final LifeCycle lifeCycle;

    public UserOnboarding() {
        this(defaultOnboardingEngine, randomUser(), defaultCreatingRequesterUsername, defaultProvisioningMode, defaultLifeCycle);
    }

    private UserOnboarding(OnboardingEngine onboardingEngine, UserDto userDto, String createdBy, ProvisioningMode provisioningMode, LifeCycle lifeCycle) {
        this.onboardingEngine = onboardingEngine;
        this.userDto = userDto;
        this.user = User.from(userDto, defaultCreatedOn, createdBy);
        this.createdBy = createdBy;
        this.provisioningMode = provisioningMode;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (provisioningMode.doOnboarding && lifeCycle == LifeCycle.PER_METHOD) {
            onboardingEngine.createUser(userDto, defaultCreatedOn, createdBy, extensionContext);
        }
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (provisioningMode.doOnboarding && lifeCycle == LifeCycle.PER_CLASS) {
            onboardingEngine.createUser(userDto, defaultCreatedOn, createdBy, extensionContext);
        }
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        if (provisioningMode.doCleanup && lifeCycle == LifeCycle.PER_METHOD) {
            onboardingEngine.deleteUser(userDto.username(), defaultDeletingRequesterUsername, extensionContext);
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        if (provisioningMode.doCleanup && lifeCycle == LifeCycle.PER_CLASS) {
            onboardingEngine.deleteUser(userDto.username(), defaultDeletingRequesterUsername, extensionContext);
        }
    }

    public Headers buildRequesterHeaders() {
        return new Headers(new Header(Tokens.REQUESTER_HEADER, userDto.username()));
    }

    public String getUsername() {
        return userDto.username();
    }

    public static String randomUsername() {
        return RandomStringUtils.randomAlphabetic(12).toLowerCase();
    }

    public static UserDto randomUser() {
        final String username = randomUsername();
        return new UserDto(username, "%s@gmail.com".formatted(username), Status.ACTIVE);
    }

    public static UserDetailedDto randomUserDetailed() {
        final String username = randomUsername();
        return new UserDetailedDto(username, "%s@gmail.com".formatted(username), Status.ACTIVE,
                defaultCreatedOn, defaultCreatedOn, defaultCreatingRequesterUsername);
    }

    public static OnboardedUserBuilder of(UserDto userDto) {
        return new OnboardedUserBuilder(userDto);
    }

    public static OnboardedUserBuilder ofRandomUser() {
        return new OnboardedUserBuilder(randomUser());
    }

    private enum LifeCycle {
        PER_CLASS,
        PER_METHOD
    }

    @RequiredArgsConstructor
    private enum ProvisioningMode {
        ALL(true, true),
        ONBOARDING_ONLY (true, false),
        CLEANUP_ONLY (false, true);
        private final boolean doOnboarding;
        private final boolean doCleanup;
    }

    public static class OnboardedUserBuilder {

        private final UserDto userDto;

        private OnboardingEngine onboardingEngine = defaultOnboardingEngine;

        private String requesterUsername = defaultDeletingRequesterUsername;

        private LifeCycle lifeCycle = defaultLifeCycle;

        private ProvisioningMode provisioningMode = defaultProvisioningMode;

        OnboardedUserBuilder(UserDto userDto) {
            this.userDto = userDto;
        }

        public OnboardedUserBuilder updatedBy(String requesterUsername) {
            this.requesterUsername = requesterUsername;
            return this;
        }

        public OnboardedUserBuilder usingRepository() {
            onboardingEngine = OnboardingEngine.REPOSITORY;
            return this;
        }

        public OnboardedUserBuilder onboardingOnly() {
            this.provisioningMode = ProvisioningMode.ONBOARDING_ONLY;
            return this;
        }

        public OnboardedUserBuilder cleanupOnly() {
            this.provisioningMode = ProvisioningMode.CLEANUP_ONLY;
            return this;
        }

        public OnboardedUserBuilder perClassOnboarding() {
            lifeCycle = LifeCycle.PER_CLASS;
            return this;
        }

        public UserOnboarding build() {
            return new UserOnboarding(onboardingEngine, userDto, requesterUsername, provisioningMode, lifeCycle);
        }
    }
}
