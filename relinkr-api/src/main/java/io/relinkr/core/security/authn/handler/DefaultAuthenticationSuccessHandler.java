package io.relinkr.core.security.authn.handler;

import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.OK;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.relinkr.core.security.authn.jwt.JwtAuthenticationService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Created by lcsontos on 5/17/17.
 */
@Slf4j
public class DefaultAuthenticationSuccessHandler
        extends AbstractAuthenticationRequestHandler implements AuthenticationSuccessHandler {

    static final int ONE_DAY_MINUTES = 24 * 60;

    private final JwtAuthenticationService jwtAuthenticationService;

    public DefaultAuthenticationSuccessHandler(
            ObjectMapper objectMapper, JwtAuthenticationService jwtAuthenticationService) {

        super(objectMapper.writer());
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        try {
            String jwtToken = jwtAuthenticationService
                    .createJwtToken(authentication, ONE_DAY_MINUTES);

            handle(response, OK, singletonMap("token", jwtToken));
        } catch (Exception e) {
            handleError(response, e);
        }
    }

}
