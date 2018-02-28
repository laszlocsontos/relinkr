package com.springuni.hermes.user;

import static org.junit.Assert.*;
import static com.springuni.hermes.user.Role.ADMIN;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        EmailAddress emailAddress = new EmailAddress("test@springuni.com");
        user = new User(emailAddress, "Test", "test");
    }

    @Test
    public void lock() {
        user.lock();
        assertTrue(user.isLocked());
    }

    @Test
    public void unlock() {
        user.lock();
        user.unlock();
        assertFalse(user.isLocked());
    }

    @Test
    public void grantRole() {
        user.grantRole(ADMIN);
        assertTrue(user.isAdmin());
    }

    @Test
    public void revokeRole() {
        user.grantRole(ADMIN);
        user.revokeRole(ADMIN);
        assertFalse(user.isAdmin());
    }

    @Test
    public void update() {
        EmailAddress emailAddress = new EmailAddress("test2@springuni.com");
        user.update(emailAddress, "Test2", "test2");
        assertEquals("test2@springuni.com", user.geEmailAddress().get().getValue());
        assertEquals("Test2", user.getName().get());
        assertEquals("test2", user.getTwitterHandle().get());
    }

}
