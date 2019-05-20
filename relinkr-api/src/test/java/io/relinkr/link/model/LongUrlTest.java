package io.relinkr.link.model;

import static io.relinkr.test.Mocks.LONG_URL_BASE;
import static io.relinkr.test.Mocks.LONG_URL_BASE_S;
import static io.relinkr.test.Mocks.LONG_URL_INVALID_UTM;
import static io.relinkr.test.Mocks.LONG_URL_VALID_UTM;
import static io.relinkr.test.Mocks.LONG_URL_VALID_UTM_S;
import static io.relinkr.test.Mocks.LONG_URL_WITHOUT_UTM;
import static io.relinkr.test.Mocks.LONG_URL_WITHOUT_UTM_S;
import static io.relinkr.test.Mocks.UTM_PARAMETERS_FULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import org.junit.Test;
import org.springframework.web.util.UriComponents;

public class LongUrlTest {

    @Test
    public void create_withLongUrlBase() {
        assertEquals(URI.create(LONG_URL_BASE_S), LONG_URL_BASE.getTargetUrl());
    }

    @Test
    public void create_withLongUrlWithoutUtm() {
        assertEquals(URI.create(LONG_URL_WITHOUT_UTM_S), LONG_URL_WITHOUT_UTM.getTargetUrl());
    }

    @Test
    public void create_withLongUrlInvalidUtm() {
        assertEquals(URI.create(LONG_URL_WITHOUT_UTM_S), LONG_URL_WITHOUT_UTM.getTargetUrl());
    }

    @Test
    public void create_withLongUrlValidUtm() {
        assertEquals(URI.create(LONG_URL_VALID_UTM_S), LONG_URL_VALID_UTM.getTargetUrl());
    }

    @Test
    public void hasUtmParameters_withLongUrlBase() {
        assertFalse(LONG_URL_BASE.hasUtmParameters());
    }

    @Test
    public void hasUtmParameters_withLongUrlWithoutUtm() {
        assertFalse(LONG_URL_WITHOUT_UTM.hasUtmParameters());
    }

    @Test
    public void hasUtmParameters_withLongUrlInvalidUtm() {
        assertFalse(LONG_URL_INVALID_UTM.hasUtmParameters());
    }

    @Test
    public void hasUtmParameters_withLongUrlValidUtm() {
        assertTrue(LONG_URL_VALID_UTM.hasUtmParameters());
    }

    @Test
    public void apply_withLongUrlBase() {
        assertEquals(UTM_PARAMETERS_FULL,
                LONG_URL_BASE.apply(UTM_PARAMETERS_FULL).getUtmParameters().get());
    }

    @Test
    public void apply_withLongUrlWithoutUtm() {
        assertEquals(UTM_PARAMETERS_FULL,
                LONG_URL_WITHOUT_UTM.apply(UTM_PARAMETERS_FULL).getUtmParameters().get());
    }

    @Test
    public void apply_withLongUrlInvalidUtm() {
        assertEquals(UTM_PARAMETERS_FULL,
                LONG_URL_INVALID_UTM.apply(UTM_PARAMETERS_FULL).getUtmParameters().get());
    }

    @Test
    public void apply_withLongUrlValidUtm() {
        assertEquals(UTM_PARAMETERS_FULL,
                LONG_URL_VALID_UTM.apply(UTM_PARAMETERS_FULL).getUtmParameters().get());
    }

    @Test(expected = InvalidUrlException.class)
    public void parseUrl_withLocalIpAddress() {
        LongUrl.parseUrl("http://10.20.53.1");
    }

    @Test
    public void parseUrl_withPublicIpAddress() {
        UriComponents uriComponents = LongUrl.parseUrl("http://216.58.214.238");
        assertEquals(uriComponents.getScheme(), "http");
        assertEquals(uriComponents.getHost(), "216.58.214.238");
    }

    @Test(expected = InvalidUrlException.class)
    public void parseUrl_withFtpUrl() {
        LongUrl.parseUrl("ftp://google.com");
    }

    @Test
    public void parseUrl_withHttpUrl() {
        UriComponents uriComponents = LongUrl.parseUrl("http://google.com");
        assertEquals(uriComponents.getScheme(), "http");
        assertEquals(uriComponents.getHost(), "google.com");
    }

    @Test
    public void parseUrl_withHttpsUrl() {
        UriComponents uriComponents = LongUrl.parseUrl("https://google.com");
        assertEquals(uriComponents.getScheme(), "https");
        assertEquals(uriComponents.getHost(), "google.com");
    }

    @Test
    public void parseUrl_withUrlFragment() {
        UriComponents uriComponents = LongUrl.parseUrl("https://google.com#test");
        assertEquals(uriComponents.getScheme(), "https");
        assertEquals(uriComponents.getHost(), "google.com");
        assertEquals(uriComponents.getFragment(), "test");
    }

}
