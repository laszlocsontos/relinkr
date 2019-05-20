package com.springuni.hermes.link.web;

import static com.springuni.hermes.link.web.LinkResourceAssembler.SHORT_LINK_DOMAIN;
import static com.springuni.hermes.link.web.LinkResourceAssembler.SHORT_LINK_SCHEME;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

@RunWith(MockitoJUnitRunner.class)
public class LinkResourceAssemblerTest {

    private static final String PATH = "0123456789";

    @Mock
    private Environment environment;

    private LinkResource linkResource;
    private LinkResourceAssembler linkResourceAssembler;

    @Before
    public void setUp() throws Exception {
        linkResource = new LinkResource("https://google.com");
        linkResourceAssembler = new LinkResourceAssembler(environment);

        RequestAttributes requestAttributes = new ServletWebRequest(new MockHttpServletRequest());
        RequestContextHolder.setRequestAttributes(requestAttributes);
    }

    @After
    public void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void givenShortLinkDomainAsProperty_whenAddShortLink_thenDomainUsed() {
        given(environment.getProperty(SHORT_LINK_SCHEME)).willReturn("https");
        linkResourceAssembler.addShortLink(linkResource, PATH);
        assertEquals("https://localhost/" + PATH, linkResource.getLink("shortLink").getHref());
    }

    @Test
    public void givenShortLinkSchemeAsProperty_whenAddShortLink_thenSchemeUsed() {
        given(environment.getProperty(SHORT_LINK_DOMAIN)).willReturn("rlr.li");
        linkResourceAssembler.addShortLink(linkResource, PATH);
        assertEquals("http://rlr.li/" + PATH, linkResource.getLink("shortLink").getHref());
    }

    @Test
    public void givenNoShortLinkProperty_whenAddShortLink_thenDefaultUsed() {
        linkResourceAssembler.addShortLink(linkResource, PATH);
        assertEquals("http://localhost/" + PATH, linkResource.getLink("shortLink").getHref());
    }

}
