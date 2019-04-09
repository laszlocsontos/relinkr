package com.springuni.hermes.test.web;

import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public abstract class BaseServletTest {

    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

}
