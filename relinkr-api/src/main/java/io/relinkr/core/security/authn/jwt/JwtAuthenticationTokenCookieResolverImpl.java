package io.relinkr.core.security.authn.jwt;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

import io.relinkr.core.web.AbstractCookieValueResolver;
import io.relinkr.core.web.CookieManager;
import io.relinkr.core.web.CookieValueResolver;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Slf4j
public class JwtAuthenticationTokenCookieResolverImpl
        implements JwtAuthenticationTokenCookieResolver {

    static final String TOKEN_PAYLOAD_COOKIE_NAME = "atp";
    static final String TOKEN_SIGNATURE_COOKIE_NAME = "ats";

    private final static String DELIMITER = ".";

    private final CookieValueResolver<String> tokenPayloadResolver;
    private final CookieValueResolver<String> tokenSignatureResolver;

    public JwtAuthenticationTokenCookieResolverImpl() {
        tokenPayloadResolver = TokenPartValueResolver.with(
                new CookieManager(TOKEN_PAYLOAD_COOKIE_NAME, null, false)
        );

        tokenSignatureResolver = TokenPartValueResolver.with(
                new CookieManager(TOKEN_SIGNATURE_COOKIE_NAME, null, true)
        );
    }

    @Override
    public Optional<String> resolveValue(HttpServletRequest request) {
        String signature = tokenSignatureResolver.resolveValue(request).orElse("");
        if (!StringUtils.hasText(signature)) {
            return Optional.empty();
        }

        // Signature should be in "header.signature" format
        int delimiterIndex = signature.indexOf(DELIMITER);
        if (delimiterIndex <= 0 || delimiterIndex >= signature.length() - 1) {
            return Optional.empty();
        }

        String payload = tokenPayloadResolver.resolveValue(request).orElse("");
        if (!StringUtils.hasText(payload)) {
            return Optional.empty();
        }

        // Reconstruct JWT in "header.payload.signature" format
        StringBuilder token = new StringBuilder(signature)
                .insert(delimiterIndex + 1, payload)
                .insert(delimiterIndex + 1 + payload.length(), DELIMITER);

        return Optional.of(token.toString());
    }

    @Override
    public void setValue(HttpServletResponse response, String token) {
        if (!StringUtils.hasText(token)) {
            tokenPayloadResolver.setValue(response, null);
            tokenSignatureResolver.setValue(response, null);
            return;
        }

        // JWTs consist of three parts (header, payload, signature) separated by dots.
        String[] parts = tokenizeToStringArray(token, DELIMITER);
        Assert.isTrue((parts.length == 3), "h.p.s format expected");

        tokenPayloadResolver.setValue(response, parts[1]);
        tokenSignatureResolver.setValue(response, parts[0] + DELIMITER + parts[2]);
    }

    @RequiredArgsConstructor(staticName = "with")
    private static class TokenPartValueResolver extends AbstractCookieValueResolver<String> {

        private final CookieManager cookieManager;

        @Override
        protected CookieManager getCookieManager() {
            return cookieManager;
        }

        @Override
        protected Optional<String> fromString(String value) {
            return Optional.ofNullable(value);
        }

        @Override
        protected Optional<String> toString(String value) {
            return Optional.ofNullable(value);
        }

    }

}
