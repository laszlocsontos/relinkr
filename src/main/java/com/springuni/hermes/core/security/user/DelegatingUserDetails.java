package com.springuni.hermes.core.security.user;

import static java.util.stream.Collectors.toList;

import com.springuni.hermes.user.model.User;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by lcsontos on 5/24/17.
 */
public class DelegatingUserDetails implements UserDetails {

    private final User delegate;

    public DelegatingUserDetails(User delegate) {
        this.delegate = delegate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(delegate.getId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !delegate.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return delegate.isConfirmed();
    }

}
