package com.springuni.hermes.visitor.web;

import static com.springuni.hermes.test.Mocks.JWS_VISITOR_COOKIE_SECRET_KEY;
import static com.springuni.hermes.test.Mocks.JWS_VISITOR_COOKIE_VALUE;
import static com.springuni.hermes.test.Mocks.VISITOR_ID;
import static com.springuni.hermes.visitor.web.VisitorIdCookieResolverImpl.COOKIE_MAX_AGE;
import static com.springuni.hermes.visitor.web.VisitorIdCookieResolverImpl.COOKIE_NAME;
import static com.springuni.hermes.visitor.web.VisitorIdCookieResolverImpl.VISITOR_SECRET_KEY_PROPERTY;

import com.springuni.hermes.core.convert.EntityClassAwareIdToStringConverter;
import com.springuni.hermes.core.convert.StringToEntityClassAwareIdConverter;
import com.springuni.hermes.core.web.AbstractCookieValueResolver;
import com.springuni.hermes.core.web.AbstractCookieValueResolverTest;
import com.springuni.hermes.visitor.model.VisitorId;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

public class VisitorIdCookieResolverTest extends AbstractCookieValueResolverTest<VisitorId> {

    private final ConfigurableConversionService conversionService;

    public VisitorIdCookieResolverTest() {
        super(COOKIE_NAME, COOKIE_MAX_AGE, VISITOR_ID, JWS_VISITOR_COOKIE_VALUE);

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
        environment.setProperty(VISITOR_SECRET_KEY_PROPERTY, JWS_VISITOR_COOKIE_SECRET_KEY);
    }

    @Override
    protected AbstractCookieValueResolver<VisitorId> createCookieValueResolver(
            Environment environment) {

        return new VisitorIdCookieResolverImpl(conversionService, environment);
    }

}
