package com.springuni.hermes.test.web;

import org.junit.Before;
import org.springframework.mock.web.MockFilterChain;

public class BaseFilterTest extends BaseServletTest {

    protected MockFilterChain filterChain;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        filterChain = new MockFilterChain();
    }

}
