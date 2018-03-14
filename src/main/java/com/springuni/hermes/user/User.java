package com.springuni.hermes.user;

import static com.springuni.hermes.user.Role.ADMIN;
import static com.springuni.hermes.user.Role.USER;

import com.springuni.hermes.core.orm.AbstractEntity;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Entity
public class User extends AbstractEntity<Long> {

    private EmailAddress emailAddress;
    private String name;
    private String twitterHandle;

    @ElementCollection
    private Set<Role> roles;

    private boolean locked;

    public User() {
        roles = new LinkedHashSet<>();
        roles.add(USER);
    }

    public User(EmailAddress emailAddress, String name, String twitterHandle) {
        this();
        update(emailAddress, name, twitterHandle);
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isAdmin() {
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }
        return roles.contains(ADMIN);
    }

    public void grantRole(Role role) {
        roles.add(role);
    }

    public void revokeRole(Role role) {
        roles.remove(role);
    }

    public Optional<EmailAddress> geEmailAddress() {
        return Optional.ofNullable(emailAddress);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getTwitterHandle() {
        return Optional.ofNullable(twitterHandle);
    }

    public void update(EmailAddress emailAddress, String name, String twitterHandle) {
        Assert.notNull(emailAddress, "email cannot be null");
        Assert.hasText(name, "eame cannot be empty");
        Assert.hasText(twitterHandle, "twitterHandle cannot be empty");

        this.emailAddress = emailAddress;
        this.name = name;
        this.twitterHandle = twitterHandle;
    }

}
