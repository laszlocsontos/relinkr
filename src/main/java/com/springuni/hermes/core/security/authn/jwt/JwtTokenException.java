package com.springuni.hermes.core.security.authn.jwt;

import org.springframework.core.NestedRuntimeException;

public class JwtTokenException extends NestedRuntimeException {

    public JwtTokenException(String msg) {
        super(msg);
    }

    public JwtTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
