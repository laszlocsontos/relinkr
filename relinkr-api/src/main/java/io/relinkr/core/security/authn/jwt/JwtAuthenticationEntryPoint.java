package io.relinkr.core.security.authn.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.relinkr.core.security.authn.handler.DefaultAuthenticationFailureHandler;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Created by lcsontos on 5/18/17.
 */
public class JwtAuthenticationEntryPoint
        extends DefaultAuthenticationFailureHandler implements AuthenticationEntryPoint {

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        onAuthenticationFailure(request, response, authException);
    }

}
