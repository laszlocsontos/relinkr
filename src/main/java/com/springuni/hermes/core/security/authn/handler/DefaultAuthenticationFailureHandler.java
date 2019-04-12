package com.springuni.hermes.core.security.authn.handler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.core.web.RestErrorResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * General authentication failure handler.
 */
@Slf4j
public class DefaultAuthenticationFailureHandler
        extends AbstractAuthenticationRequestHandler implements AuthenticationFailureHandler {

    public DefaultAuthenticationFailureHandler(ObjectMapper objectMapper) {
        super(objectMapper.writer());
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception)
            throws IOException {

        log.warn(exception.getMessage());

        HttpStatus httpStatus = translateAuthenticationException(exception);
        handle(response, httpStatus, RestErrorResponse.of(httpStatus, exception));
    }

    private HttpStatus translateAuthenticationException(AuthenticationException exception) {
        if (exception instanceof InternalAuthenticationServiceException) {
            return INTERNAL_SERVER_ERROR;
        }

        if (exception instanceof AuthenticationServiceException) {
            return SERVICE_UNAVAILABLE;
        }

        return UNAUTHORIZED;
    }

}
