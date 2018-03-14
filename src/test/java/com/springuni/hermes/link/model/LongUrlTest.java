package com.springuni.hermes.link.model;

import static com.springuni.hermes.Mocks.LONG_URL_BASE;
import static com.springuni.hermes.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.Mocks.LONG_URL_INVALID_UTM;
import static com.springuni.hermes.Mocks.LONG_URL_VALID_UTM;
import static com.springuni.hermes.Mocks.LONG_URL_VALID_UTM_S;
import static com.springuni.hermes.Mocks.LONG_URL_WITHOUT_UTM;
import static com.springuni.hermes.Mocks.LONG_URL_WITHOUT_UTM_S;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_FULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import org.junit.Test;

public class LongUrlTest {

    @Test
    public void create_withLongUrlBase() throws Exception {
        assertEquals(new URL(LONG_URL_BASE_S), LONG_URL_BASE.getTargetUrl());
    }

    @Test
    public void create_withLongUrlWithoutUtm() throws Exception {
        assertEquals(new URL(LONG_URL_WITHOUT_UTM_S), LONG_URL_WITHOUT_UTM.getTargetUrl());
    }

    @Test
    public void create_withLongUrlInvalidUtm() throws Exception {
        assertEquals(new URL(LONG_URL_WITHOUT_UTM_S), LONG_URL_WITHOUT_UTM.getTargetUrl());
    }

    @Test
    public void create_withLongUrlValidUtm() throws Exception {
        assertEquals(new URL(LONG_URL_VALID_UTM_S), LONG_URL_VALID_UTM.getTargetUrl());
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
                LONG_URL_BASE.apply(UTM_PARAMETERS_FULL).getUtmParameters());
    }

    @Test
    public void apply_withLongUrlWithoutUtm() {
        assertEquals(UTM_PARAMETERS_FULL,
                LONG_URL_WITHOUT_UTM.apply(UTM_PARAMETERS_FULL).getUtmParameters());
    }

    @Test
    public void apply_withLongUrlInvalidUtm() {
        assertEquals(UTM_PARAMETERS_FULL,
                LONG_URL_INVALID_UTM.apply(UTM_PARAMETERS_FULL).getUtmParameters());
    }

    @Test
    public void apply_withLongUrlValidUtm() {
        assertEquals(UTM_PARAMETERS_FULL,
                LONG_URL_VALID_UTM.apply(UTM_PARAMETERS_FULL).getUtmParameters());
    }

}