package com.springuni.hermes.core.security.user;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.springuni.hermes.Mocks;
import com.springuni.hermes.user.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DelegatingUserDetailsTest {

    private User user;
    private UserDetails userDetails;

    @Before
    public void setUp() {
        user = Mocks.createUser();
        userDetails = new DelegatingUserDetails(user);
    }

    @Test
    public void getAuthorities() {
        assertThat(
                userDetails.getAuthorities(),
                containsInAnyOrder(user.getRoles()
                        .stream().map(it -> new SimpleGrantedAuthority(it.name())).toArray()
                )
        );
    }

    @Test
    public void getPassword() {
        assertNull(userDetails.getPassword());
    }

    @Test
    public void getUsername() {
        assertEquals(String.valueOf(user.getId()), userDetails.getUsername());
    }

    @Test
    public void isAccountNonExpired() {
        assertFalse(userDetails.isAccountNonExpired());
    }

    @Test
    public void isAccountNonLocked() {
        assertEquals(!user.isLocked(), userDetails.isAccountNonLocked());
    }

    @Test
    public void isCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void isEnabled() {
        assertEquals(user.isConfirmed(), userDetails.isEnabled());
    }

}
