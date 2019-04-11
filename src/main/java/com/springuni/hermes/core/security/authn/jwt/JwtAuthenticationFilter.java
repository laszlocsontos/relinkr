package com.springuni.hermes.core.security.authn.jwt;

import static java.util.Optional.empty;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Created by lcsontos on 5/18/17.
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";

    private final JwtAuthenticationService jwtAuthenticationService;

    public JwtAuthenticationFilter(JwtAuthenticationService jwtAuthenticationService) {
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            Optional<Authentication> authentication = getAuthentication(request);
            authentication.ifPresent(getContext()::setAuthentication);

            filterChain.doFilter(request, response);
        } finally {
            clearContext();
        }
    }

    private Optional<Authentication> getAuthentication(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isEmpty(authorizationHeader)) {
            log.debug("Authorization header is empty.");
            return empty();
        }

        if (!StringUtils.substringMatch(authorizationHeader, 0, TOKEN_PREFIX)) {
            log.debug("Token prefix {} in Authorization header was not found.", TOKEN_PREFIX);
            return empty();
        }

        String jwtToken = authorizationHeader.substring(TOKEN_PREFIX.length() + 1);

        try {
            return Optional.of(jwtAuthenticationService.parseJwtToken(jwtToken));
        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
            return empty();
        }
    }

}
