package io.relinkr.core.security.authn.user;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserIdAuthenticationToken extends AbstractAuthenticationToken {

    private final long userId;

    private UserIdAuthenticationToken(
            long userId, Collection<? extends GrantedAuthority> authorities) {

        super(authorities);
        setAuthenticated(true);
        this.userId = userId;
    }

    public static UserIdAuthenticationToken of(long userId,
            Collection<? extends GrantedAuthority> authorities) {
        return new UserIdAuthenticationToken(userId, authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Long getPrincipal() {
        return userId;
    }

}
