package io.relinkr.visitor.web;

import static io.relinkr.test.Mocks.JWS_VISITOR_COOKIE_SECRET_KEY;
import static io.relinkr.test.Mocks.JWS_VISITOR_COOKIE_VALUE;
import static io.relinkr.test.Mocks.VISITOR_ID;

import io.relinkr.core.convert.EntityClassAwareIdToStringConverter;
import io.relinkr.core.convert.StringToEntityClassAwareIdConverter;
import io.relinkr.core.web.AbstractCookieValueResolver;
import io.relinkr.core.web.AbstractCookieValueResolverTest;
import io.relinkr.visitor.model.VisitorId;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

public class VisitorIdCookieResolverTest extends AbstractCookieValueResolverTest<VisitorId> {

    private final ConfigurableConversionService conversionService;

    public VisitorIdCookieResolverTest() {
        super(VisitorIdCookieResolverImpl.COOKIE_NAME, VisitorIdCookieResolverImpl.COOKIE_MAX_AGE,
                VISITOR_ID, JWS_VISITOR_COOKIE_VALUE);

        conversionService = new GenericConversionService();

        conversionService.addConverter(
                String.class,
                VisitorId.class,
                new StringToEntityClassAwareIdConverter<>(VisitorId.class)
        );

        conversionService.addConverter(new EntityClassAwareIdToStringConverter<VisitorId>());
    }

    @Override
    protected void setUpEnvironment(MockEnvironment environment) {
        environment.setProperty(VisitorIdCookieResolverImpl.VISITOR_SECRET_KEY_PROPERTY,
                JWS_VISITOR_COOKIE_SECRET_KEY);
    }

    @Override
    protected AbstractCookieValueResolver<VisitorId> createCookieValueResolver(
            Environment environment) {

        return new VisitorIdCookieResolverImpl(conversionService, environment);
    }

}
