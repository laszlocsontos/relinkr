package com.springuni.hermes.core.security.user;

import com.springuni.hermes.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

/**
 * Created by lcsontos on 5/24/17.
 */
public class DelegatingUserDetailsService implements UserDetailsService {

    private final UserService delegate;

    public DelegatingUserDetailsService(UserService delegate) {
        this.delegate = delegate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("Empty user name");
        }

        long userId;
        try {
            userId = Long.valueOf(username);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }

        return delegate.findUser(userId)
                .map(DelegatingUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
