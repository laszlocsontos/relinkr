package com.springuni.hermes.core.security.userdetails;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import com.springuni.hermes.Mocks;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.user.service.UserService;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingUserDetailsServiceTest {

    @Mock
    private UserService userService;

    private User user;
    private UserDetailsService userDetailsService;

    @Before
    public void setUp() throws Exception {
        user = Mocks.createUser();
        userDetailsService = new DelegatingUserDetailsService(userService);
    }

    @Test
    public void loadUserByUsername() {
        given(userService.findUser(user.getId())).willReturn(Optional.of(user));

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(String.valueOf(user.getId()));

        assertEquals(String.valueOf(user.getId()), userDetails.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_withEmptyUserName() {
        userDetailsService.loadUserByUsername("");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_withNonNumericUserName() {
        userDetailsService.loadUserByUsername("bad");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_withNonExistentUser() {
        given(userService.findUser(user.getId())).willReturn(Optional.empty());
        userDetailsService.loadUserByUsername(String.valueOf(user.getId()));
    }

}
