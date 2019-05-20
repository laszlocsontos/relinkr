package com.springuni.hermes.core.security.authn.oauth2;

import static com.springuni.hermes.core.security.authn.oauth2.AbstractOAuth2LoginAuthenticationHandler.FRONTEND_LOGIN_URL_PROPERTY;

import com.springuni.hermes.test.web.BaseServletTest;
import org.junit.Before;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

abstract class AbstractOAuth2LoginAuthenticationHandlerTest<H extends AbstractOAuth2LoginAuthenticationHandler>
        extends BaseServletTest {

    static final String LOGIN_URL = "https://app.relinkr.com/login";

    H handler;

    @Before
    public void setUp() {
        super.setUp();

        handler = createHandler();

        Environment environment = new MockEnvironment()
                .withProperty(FRONTEND_LOGIN_URL_PROPERTY, LOGIN_URL);

        handler.setEnvironment(environment);
    }

    abstract H createHandler();

}
