package com.endava.internship.cryptomarket.confservice.data;

import com.endava.internship.cryptomarket.confservice.data.model.Roles;
import com.endava.internship.cryptomarket.confservice.data.model.Status;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class UsersInMemRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UsersInMemRepository();
        userRepository.save(User.builder().username("operat1").role(Roles.OPERAT)
                .email("operat1@gmail.com").status(Status.ACTIVE)
                .createdOn(now()).build());
        userRepository.save(User.builder().username("operat2").role(Roles.OPERAT)
                .email("operat2@gmail.com").status(Status.ACTIVE)
                .createdOn(now()).build());
        userRepository.save(User.builder().username("operat3").role(Roles.OPERAT)
                .email("operat3@gmail.com").status(Status.SUSPND)
                .createdOn(now()).build());
        userRepository.save(User.builder().username("operat4").role(Roles.OPERAT)
                .email("operat4@gmail.com").status(Status.INACTV)
                .createdOn(now()).build());
        userRepository.save(User.builder().username("client1").role(Roles.CLIENT)
                .email("client@gmail.com").status(Status.ACTIVE)
                .createdOn(now()).build());
    }

    @Test
    void whenGetAll_thenReturnAllUsersList() {
        List<User> userList = List.of(
                User.builder().username("admin").role(Roles.ADMIN)
                        .email("admin@gmail.com").status(Status.ACTIVE)
                        .createdOn(now()).build(),
                User.builder().username("operat1").role(Roles.OPERAT)
                        .email("operat1@gmail.com").status(Status.ACTIVE)
                        .createdOn(now()).build(),
                User.builder().username("operat2").role(Roles.OPERAT)
                        .email("operat2@gmail.com").status(Status.ACTIVE)
                        .createdOn(now()).build(),
                User.builder().username("operat3").role(Roles.OPERAT)
                        .email("operat3@gmail.com").status(Status.SUSPND)
                        .createdOn(now()).build(),
                User.builder().username("operat4").role(Roles.OPERAT)
                        .email("operat4@gmail.com").status(Status.INACTV)
                        .createdOn(now()).build(),
                User.builder().username("client1").role(Roles.CLIENT)
                        .email("client@gmail.com").status(Status.ACTIVE)
                        .createdOn(now()).build()
        );

        assertThat(userRepository.getAll()).containsExactlyInAnyOrderElementsOf(userList);
    }

    @Test
    void whenGetUser_thenReturnItFromRepository() {
        Optional<User> user = Optional.of(User.builder().username("operat1").role(Roles.OPERAT)
                .email("operat1@gmail.com").status(Status.ACTIVE)
                .createdOn(now()).build());

        assertThat(user).isEqualTo(userRepository.get("operat1"));
    }

    @Test
    void whenSaveUser_thenGetUserReturnsIt() {
        User user = User.builder().username("operat5").role(Roles.OPERAT)
                .email("operat5@gmail.com").status(Status.ACTIVE)
                .createdOn(now()).build();

        userRepository.save(user);

        assertThat(user).isEqualTo(userRepository.get("operat5").get());
    }

    @Test
    void whenSaveUserExistentUsr_thenReturnFalse() {
        User user = User.builder().username("operat1").role(Roles.OPERAT)
                .email("operat1@gmail.com").status(Status.ACTIVE)
                .createdOn(now()).build();

        assertThat(userRepository.save(user)).isFalse();
    }

    @Test
    void whenDeleteExistentUser_thenReturnTrueAndUserIsNotFound() {
        String username = "operat1";

        assertThat(userRepository.delete(username)).isTrue();

        assertThat(userRepository.get(username)).isEmpty();
    }

    @Test
    void whenDeleteNonexistentUser_thenReturnFalse() {
        String username = "operat5";

        assertThat(userRepository.delete(username)).isFalse();
    }

}
