package com.springuni.hermes.core.security.authn.oauth2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.core.web.AbstractCookieValueResolver;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2AuthorizationRequestCookieResolverImpl extends
        AbstractCookieValueResolver<Map<String, OAuth2AuthorizationRequest>>
        implements OAuth2AuthorizationRequestsCookieResolver {

    static final String COOKIE_NAME = "oar";

    static final Duration COOKIE_MAX_AGE = Duration.ofMinutes(5);

    static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_SECRET_KEY_PROPERTY =
            "craftingjava.relinkr.cookies.oauth2-request-secret-key";

    private final ObjectMapper objectMapper;

    public OAuth2AuthorizationRequestCookieResolverImpl() {
        super(COOKIE_NAME, COOKIE_MAX_AGE, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_SECRET_KEY_PROPERTY);

        // We want to leave the ObjectMapper in the app-context unchanged
        objectMapper = new ObjectMapper().addMixIn(
                OAuth2AuthorizationRequest.class, OAuth2AuthorizationRequestMixIn.class
        );
    }

    @Override
    protected Optional<Map<String, OAuth2AuthorizationRequest>> fromString(String value) {
        if (value == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(
                    objectMapper.readValue(
                            value,
                            new TypeReference<Map<String, OAuth2AuthorizationRequest>>() {}
                    )
            );
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    protected Optional<String> toString(Map<String, OAuth2AuthorizationRequest> value) {
        if (value == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.writeValueAsString(value));
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
            return Optional.empty();
        }
    }

}
