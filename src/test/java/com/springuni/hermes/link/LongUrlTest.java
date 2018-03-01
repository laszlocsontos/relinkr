package com.springuni.hermes.link;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import org.junit.Before;
import org.junit.Test;

public class LongUrlTest {

    private static final String LONG_URL_BASE
            = "https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html";

    private static final String LONG_URL_FRAGMENT = "#append-java.lang.String-";
    private static final String LONG_URL_WITHOUT_UTM = LONG_URL_BASE + LONG_URL_FRAGMENT;
    private static final String LONG_URL_VALID_UTM =
            LONG_URL_BASE + "?utm_source=source&utm_medium=medium&utm_campaign=campaign" +
                    LONG_URL_FRAGMENT;

    private static final String LONG_URL_INVALID_UTM =
            LONG_URL_BASE + "?utm_source=source" + LONG_URL_FRAGMENT;

    private LongUrl longUrlBase;
    private LongUrl longUrlWithoutUtm;
    private LongUrl longUrlValidUtm;
    private LongUrl longUrlInvalidUtm;

    private UtmParameters utmParameters;

    @Before
    public void setUp() throws Exception {
        longUrlBase = new LongUrl(LONG_URL_BASE);
        longUrlWithoutUtm = new LongUrl(LONG_URL_WITHOUT_UTM);
        longUrlValidUtm = new LongUrl(LONG_URL_VALID_UTM);
        longUrlInvalidUtm = new LongUrl(LONG_URL_INVALID_UTM);

        utmParameters =
                new UtmParameters("source", "medium", "campaign");
    }

    @Test
    public void create_withLongUrlBase() throws Exception {
        assertEquals(new URL(LONG_URL_BASE), longUrlBase.getTargetUrl());
    }

    @Test
    public void create_withLongUrlWithoutUtm() throws Exception {
        assertEquals(new URL(LONG_URL_WITHOUT_UTM), longUrlWithoutUtm.getTargetUrl());
    }

    @Test
    public void create_withLongUrlInvalidUtm() throws Exception {
        assertEquals(new URL(LONG_URL_WITHOUT_UTM), longUrlInvalidUtm.getTargetUrl());
    }

    @Test
    public void create_withLongUrlValidUtm() throws Exception {
        assertEquals(new URL(LONG_URL_VALID_UTM), longUrlValidUtm.getTargetUrl());
    }

    @Test
    public void hasUtmParameters_withLongUrlBase() {
        assertFalse(longUrlBase.hasUtmParameters());
    }

    @Test
    public void hasUtmParameters_withLongUrlWithoutUtm() {
        assertFalse(longUrlWithoutUtm.hasUtmParameters());
    }

    @Test
    public void hasUtmParameters_withLongUrlInvalidUtm() {
        assertFalse(longUrlInvalidUtm.hasUtmParameters());
    }

    @Test
    public void hasUtmParameters_withLongUrlValidUtm() {
        assertTrue(longUrlValidUtm.hasUtmParameters());
    }

    @Test
    public void apply_withLongUrlBase() {
        assertEquals(utmParameters, longUrlBase.apply(utmParameters).getUtmParameters());
    }

    @Test
    public void apply_withLongUrlWithoutUtm() {
        assertEquals(utmParameters, longUrlWithoutUtm.apply(utmParameters).getUtmParameters());
    }

    @Test
    public void apply_withLongUrlInvalidUtm() {
        assertEquals(utmParameters, longUrlInvalidUtm.apply(utmParameters).getUtmParameters());
    }

    @Test
    public void apply_withLongUrlValidUtm() {
        assertEquals(utmParameters, longUrlValidUtm.apply(utmParameters).getUtmParameters());
    }

}
