package com.springuni.hermes.core.security.user;

import static com.springuni.hermes.Mocks.createUser;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import com.springuni.hermes.user.NoSuchUserException;
import com.springuni.hermes.user.UnconfirmedUserException;
import com.springuni.hermes.user.User;
import com.springuni.hermes.user.UserLockedException;
import com.springuni.hermes.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UsernamePasswordAuthenticationProviderTest {

    private static final String USER = "user";
    private static final String PASSWORD = "password";

    @Mock
    private UserService userService;

    private Authentication testAuthentication;
    private AuthenticationProvider provider;
    private User user;

    @Before
    public void setUp() throws Exception {
        testAuthentication = new TestingAuthenticationToken(USER, PASSWORD);
        provider = new UsernamePasswordAuthenticationProvider(userService);
        user = createUser();
    }

    @Test
    public void authenticate() {
        given(userService.login(USER, PASSWORD)).willReturn(user);
        Authentication authentication = provider.authenticate(testAuthentication);
        assertEquals(user.getId(), authentication.getPrincipal());
        assertThat(
                authentication.getAuthorities(),
                containsInAnyOrder(user.getRoles()
                        .stream().map(it -> new SimpleGrantedAuthority(it.name())).toArray()
                )
        );
    }

    @Test(expected = UsernameNotFoundException.class)
    public void authenticate_withNoSuchUser() {
        given(userService.login(USER, PASSWORD)).willThrow(NoSuchUserException.class);
        provider.authenticate(testAuthentication);
    }

    @Test(expected = DisabledException.class)
    public void authenticate_withUnconfirmedUser() {
        given(userService.login(USER, PASSWORD)).willThrow(UnconfirmedUserException.class);
        provider.authenticate(testAuthentication);
    }

    @Test(expected = LockedException.class)
    public void authenticate_withLockedUser() {
        given(userService.login(USER, PASSWORD)).willThrow(UserLockedException.class);
        provider.authenticate(testAuthentication);
    }

}
