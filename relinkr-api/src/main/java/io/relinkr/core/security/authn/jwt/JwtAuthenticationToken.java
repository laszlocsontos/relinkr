package io.relinkr.core.security.authn.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Created by lcsontos on 5/17/17.
 */
class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String bearerToken;

    private JwtAuthenticationToken(String bearerToken) {
        super(null);
        setAuthenticated(false);
        this.bearerToken = bearerToken;
    }

    static JwtAuthenticationToken of(String bearerToken) {
        return new JwtAuthenticationToken(bearerToken);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public String getPrincipal() {
        return bearerToken;
    }

}
