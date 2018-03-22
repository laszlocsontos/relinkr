package com.springuni.hermes.user.model;

import static com.springuni.hermes.Mocks.createUser;
import static com.springuni.hermes.user.model.Role.ADMIN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        EmailAddress emailAddress = new EmailAddress("test@springuni.com");
        user = createUser();
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
