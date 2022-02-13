package com.endava.upskill.confservice.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.endava.upskill.confservice.domain.model.create.UserDto;
import com.endava.upskill.confservice.domain.model.entity.User;
import com.endava.upskill.confservice.domain.model.get.UserDetailedDto;
import com.endava.upskill.confservice.domain.model.shared.Priorities;
import com.endava.upskill.confservice.domain.model.update.UserUpdateDto;

import static com.endava.upskill.confservice.domain.model.exception.ExceptionResponse.USER_NOT_REMOVABLE;
import static com.endava.upskill.confservice.domain.model.exception.ExceptionResponse.USER_NOT_UPDATABLE;

@Validated(Priorities.class)
public interface UserService {

    List<UserDto> getAllUsers();

    UserDetailedDto getUser(String username);

    void createUser(String requesterUsername, @Valid UserDto user, LocalDateTime localDateTime);

    @RequesterUserCanUpdateSelfOnly(groups = Priorities.M2.class) //TODO test
    @PathUsernameEqualsBodyUsername(groups = Priorities.M3.class)
    @RequesterUserCannotUpdateStatus(groups = Priorities.M4.class) //TODO test
    void updateUser(
            String requesterUsername,
            @NotAdmin(groups = Priorities.M1.class, exceptionResponse = USER_NOT_UPDATABLE)
                    String username,
            @Valid
                    UserUpdateDto userUpdateDto,
            LocalDateTime localDateTime);

    void deleteUser(
            String requesterUsername,
            @NotAdmin(groups = Priorities.M1.class, exceptionResponse = USER_NOT_REMOVABLE)
                    String username);

    Optional<User> getRequester(String username);
}
