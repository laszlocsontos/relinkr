package com.springuni.hermes.visitor.web;

import static org.springframework.util.Assert.isTrue;

import com.springuni.hermes.core.web.AbstractCookieValueResolver;
import com.springuni.hermes.visitor.model.VisitorId;
import java.time.Duration;
import java.util.Optional;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
public class VisitorIdCookieResolverImpl
        extends AbstractCookieValueResolver<VisitorId> implements VisitorIdCookieResolver {

    static final String COOKIE_NAME = "vid";

    // Three years
    static final Duration COOKIE_MAX_AGE = Duration.ofDays(3 * 365);

    static final String VISITOR_SECRET_KEY_PROPERTY =
            "craftingjava.relinkr.cookies.visitor-secret-key";

    private final ConversionService conversionService;

    public VisitorIdCookieResolverImpl(ConversionService conversionService) {
        super(COOKIE_NAME, COOKIE_MAX_AGE, VISITOR_SECRET_KEY_PROPERTY);

        assertCanConvert(conversionService, String.class, VisitorId.class);
        assertCanConvert(conversionService, VisitorId.class, String.class);
        this.conversionService = conversionService;
    }

    private void assertCanConvert(
            ConversionService conversionService, Class<?> sourceType, Class<?> targetType) {

        isTrue(
                conversionService.canConvert(sourceType, targetType),
                "conversionService is not configured for converting ["
                        + sourceType.getSimpleName() + "] to [" + targetType.getSimpleName() + "]"
        );
    }

    @Override
    protected Optional<VisitorId> fromString(String value) {
        return Optional.ofNullable(convert(value, VisitorId.class));
    }

    @Override
    protected Optional<String> toString(VisitorId value) {
        return Optional.ofNullable(convert(value, String.class));
    }

    private <S, T> T convert(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }

        try {
            return conversionService.convert(source, targetClass);
        } catch (ConversionFailedException e) {
            // Conversion failed for whatever reason
            return null;
        }
    }

}
