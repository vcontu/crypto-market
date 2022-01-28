package com.endava.upskill.confservice.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import com.endava.upskill.confservice.domain.dao.UserRepository;
import com.endava.upskill.confservice.domain.model.user.Status;
import com.endava.upskill.confservice.domain.model.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AdminConfig {

    public static final String ADMIN_USERNAME = "admin";

    private final UserRepository userRepository;

    private final Clock clock;

    @EventListener(classes = ContextRefreshedEvent.class)
    public void adminUserInit() {
        final Optional<User> admin = userRepository.get(ADMIN_USERNAME);
        if (admin.isEmpty()) {
            log.info("Creating {} user", ADMIN_USERNAME);
            final User newAdminUser = User.builder()
                    .username(ADMIN_USERNAME)
                    .email("admin@gmail.com")
                    .status(Status.ACTIVE)
                    .createdOn(LocalDateTime.now(clock))
                    .updatedOn(LocalDateTime.now(clock))
                    .updatedBy("system")
                    .build();
            userRepository.save(newAdminUser);
        }
    }
}