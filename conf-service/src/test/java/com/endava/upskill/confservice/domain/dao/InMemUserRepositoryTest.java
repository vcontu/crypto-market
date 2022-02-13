package com.endava.upskill.confservice.domain.dao;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

import static java.time.LocalDateTime.now;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.endava.upskill.confservice.domain.model.entity.Status;
import com.endava.upskill.confservice.domain.model.entity.User;
import com.endava.upskill.confservice.persistence.InMemUserRepository;

import static com.endava.upskill.confservice.util.Tokens.CLOCK_CONF_SERVICE;

import lombok.Getter;

class InMemUserRepositoryTest {

    private final InMemUserRepository userRepository = new InMemUserRepository();

    private enum TestUsers {
        U01("operat1", "operat1@gmail.com", Status.ACTIVE, now(CLOCK_CONF_SERVICE)),
        U02("operat2", "operat2@gmail.com", Status.ACTIVE, now(CLOCK_CONF_SERVICE)),
        U03("operat3", "operat3@gmail.com", Status.SUSPND, now(CLOCK_CONF_SERVICE)),
        U04("operat4", "operat4@gmail.com", Status.INACTV, now(CLOCK_CONF_SERVICE));

        @Getter
        private final User user;

        TestUsers(String username, String email, Status status, LocalDateTime createdOn) {
            user = User.builder()
                    .username(username)
                    .email(email)
                    .status(status)
                    .createdOn(createdOn)
                    .build();
        }
    }

    @BeforeEach
    void setUp() {
        EnumSet.allOf(TestUsers.class).forEach(testUser -> userRepository.save(testUser.user));
    }

    @Test
    void whenGetAll_thenReturnAllUsersList() {
        final List<User> testUsers = EnumSet.allOf(TestUsers.class).stream().map(TestUsers::getUser)
                .toList();
        assertThat(userRepository.getAll()).containsExactlyInAnyOrderElementsOf(testUsers);
    }

    @Test
    void whenGetUser_thenReturnItFromRepository() {
        final User expectedUser = TestUsers.U01.getUser();
        assertThat(userRepository.get("operat1")).contains(expectedUser);
    }

    @Test
    void whenSaveUser_thenGetUserReturnsIt() {
        final User user = User.builder()
                .username("operat5")
                .email("operat5@gmail.com")
                .status(Status.ACTIVE)
                .createdOn(now(CLOCK_CONF_SERVICE))
                .build();

        final boolean saveWasSuccessful = userRepository.save(user);

        assertAll(() -> assertThat(saveWasSuccessful).isTrue(),
                () -> assertThat(userRepository.get("operat5")).contains(user));
    }

    @Test
    void whenSaveExistingUser_thenReturnFalse() {
        final User user = TestUsers.U01.getUser();
        assertThat(userRepository.save(user)).isFalse();
    }

    @Test
    void whenDeleteExistentUser_thenReturnTrueAndUserIsNotFound() {
        final boolean userTobeDeletedExists = userRepository.delete("operat1");
        assertThat(userTobeDeletedExists).isTrue();
        assertThat(userRepository.get("operat1")).isEmpty();
    }

    @Test
    void whenDeleteNonexistentUser_thenReturnFalse() {
        final boolean userTobeDeletedExists = userRepository.delete("operat5");
        assertThat(userTobeDeletedExists).isFalse();
    }
}
