package com.endava.internship.cryptomarket.confservice.api.datahandlers;

import com.endava.internship.cryptomarket.confservice.business.UserService;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.CLIENT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StringToUserDtoConverterTest {

    private final String username = "client";
    @Mock
    private UserService userService;
    @InjectMocks
    private StringToUserConverter stringToUserConverter;
    private User user;

    @Test
    void whenConvetString_thenReturnUserDTO() {
        user = User.builder().username(username).email("client@gmail.com").role(CLIENT).
                status(ACTIVE).build();
        when(userService.getRequesterUser(username)).thenReturn(ofNullable(user));

        Optional<User> actualUser = stringToUserConverter.convert(username);

        assertThat(user).isEqualTo(actualUser.get());
        verify(userService).getRequesterUser(username);
    }

    @Test
    void whenConvetStringOfInexistentUser_thenReturnUserWithOnlyUsernameField() {
        user = User.builder().username(username).build();
        when(userService.getRequesterUser(username)).thenReturn(Optional.empty());

        Optional<User> actualUser = stringToUserConverter.convert(username);

        assertThat(user).isEqualTo(actualUser.get());
        verify(userService).getRequesterUser(username);
    }

}
