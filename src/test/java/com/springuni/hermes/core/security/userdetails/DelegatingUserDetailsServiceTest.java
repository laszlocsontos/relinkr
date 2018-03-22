package com.springuni.hermes.core.security.userdetails;

import static com.springuni.hermes.Mocks.createUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import com.springuni.hermes.user.model.EmailAddress;
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
    public void setUp() {
        user = createUser();
        userDetailsService = new DelegatingUserDetailsService(userService);
    }

    @Test
    public void loadUserByUsername() {
        EmailAddress emailAddress = user.getEmailAddress().get();
        given(userService.findUser(emailAddress)).willReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(emailAddress.getValue());

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
        EmailAddress emailAddress = user.getEmailAddress().get();
        given(userService.findUser(emailAddress)).willReturn(Optional.empty());
        userDetailsService.loadUserByUsername(emailAddress.getValue());
    }

}
