package com.springuni.hermes.visitor.web;

import static org.springframework.util.Assert.isTrue;

import com.springuni.hermes.core.web.CookieManager;
import com.springuni.hermes.core.web.JwsCookieManager;
import com.springuni.hermes.visitor.model.VisitorId;
import java.time.Duration;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class VisitorIdResolverImpl implements VisitorIdResolver {

    static final String COOKIE_NAME = "vid";

    // Three years
    static final Duration COOKIE_MAX_AGE = Duration.ofDays(3 * 365);

    static final String VISITOR_SECRET_KEY_PROPERTY =
            "craftingjava.relinkr.cookies.visitor-secret-key";

    private final ConversionService conversionService;
    private final CookieManager cookieManager;

    public VisitorIdResolverImpl(ConversionService conversionService, Environment environment) {
        assertCanConvert(conversionService, String.class, VisitorId.class);
        assertCanConvert(conversionService, VisitorId.class, String.class);
        this.conversionService = conversionService;

        String secretKey = environment.getRequiredProperty(VISITOR_SECRET_KEY_PROPERTY);
        cookieManager = new JwsCookieManager(COOKIE_NAME, COOKIE_MAX_AGE, secretKey);
    }

    @Override
    public Optional<VisitorId> resolveVisitorId(HttpServletRequest request) {
        return cookieManager.getCookie(request).map(it -> convert(it, VisitorId.class));
    }

    @Override
    public void setVisitorId(HttpServletResponse response, VisitorId visitorId) {
        Optional<String> cookieValue = Optional.ofNullable(visitorId)
                .map(it -> convert(it, String.class));

        if (cookieValue.isPresent()) {
            cookieManager.addCookie(response, cookieValue.get());
            return;
        }

        cookieManager.removeCookie(response);
    }

    private void assertCanConvert(
            ConversionService conversionService, Class<?> sourceType, Class<?> targetType) {

        isTrue(
                conversionService.canConvert(sourceType, targetType),
                "conversionService is not configured for converting ["
                        + sourceType.getSimpleName() + "] to [" + targetType.getSimpleName() + "]"
        );
    }

    private <S, T> T convert(S source, Class<T> targetClass) {
        try {
            return conversionService.convert(source, targetClass);
        } catch (ConversionFailedException e) {
            // Conversion failed for whatever reason
            return null;
        }
    }

}
