package com.springuni.hermes.user.model;

import static com.springuni.hermes.user.model.Role.ADMIN;
import static com.springuni.hermes.user.model.Role.USER;
import static java.util.Collections.unmodifiableSet;

import com.springuni.hermes.core.orm.AbstractEntity;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "user_")
public class User extends AbstractEntity<Long> {

    private EmailAddress emailAddress;
    private String encryptedPassword;
    private String name;
    private String twitterHandle;

    @ElementCollection
    private Set<Role> roles;

    private boolean confirmed;
    private boolean locked;


    public User() {
        roles = new LinkedHashSet<>();
        roles.add(USER);
    }

    public User(
            EmailAddress emailAddress, String encryptedPassword, String name,
            String twitterHandle) {
        this();
        // TODO
        this.encryptedPassword = encryptedPassword;
        update(emailAddress, name, twitterHandle);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void confirm() {
        confirmed = true;
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

    public Set<Role> getRoles() {
        return unmodifiableSet(roles);
    }

    public void grantRole(Role role) {
        roles.add(role);
    }

    public void revokeRole(Role role) {
        roles.remove(role);
    }

    public Optional<EmailAddress> getEmailAddress() {
        return Optional.ofNullable(emailAddress);
    }

    public Optional<String> getEncryptedPassword() {
        return Optional.ofNullable(encryptedPassword);
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
