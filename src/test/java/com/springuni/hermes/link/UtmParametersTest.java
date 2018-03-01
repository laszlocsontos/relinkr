package com.springuni.hermes.link;

import static com.springuni.hermes.link.UtmParameters.UTM_CAMPAIGN;
import static com.springuni.hermes.link.UtmParameters.UTM_CONTENT;
import static com.springuni.hermes.link.UtmParameters.UTM_MEDIUM;
import static com.springuni.hermes.link.UtmParameters.UTM_SOURCE;
import static com.springuni.hermes.link.UtmParameters.UTM_TERM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Map;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Test;

public class UtmParametersTest {

    private UtmParameters utmParameters;

    @Before
    public void setUp() throws Exception {
        utmParameters = new UtmParameters(
                "test1",
                "test2",
                "test3",
                "test4",
                "test5"
        );
    }

    @Test
    public void asMap() {
        Map<String, String> utmParameterMap = utmParameters.asMap();
        assertThat(utmParameterMap, IsMapContaining.hasEntry(UTM_SOURCE, "test1"));
        assertThat(utmParameterMap, IsMapContaining.hasEntry(UTM_MEDIUM, "test2"));
        assertThat(utmParameterMap, IsMapContaining.hasEntry(UTM_CAMPAIGN, "test3"));
        assertThat(utmParameterMap, IsMapContaining.hasEntry(UTM_TERM, "test4"));
        assertThat(utmParameterMap, IsMapContaining.hasEntry(UTM_CONTENT, "test5"));
    }

    @Test(expected = MissingUtmParameterException.class)
    public void create_withMissingUtmSource() throws Exception {
        new UtmParameters(null, "test", "test");
    }

    @Test(expected = MissingUtmParameterException.class)
    public void create_withMissingUtmMedium() throws Exception {
        new UtmParameters("test", null, "test");
    }

    @Test(expected = MissingUtmParameterException.class)
    public void create_withMissingUtmCampaign() throws Exception {
        new UtmParameters("test", "test", null);
    }

    @Test
    public void getUtmSource() {
        assertEquals("test1", utmParameters.getUtmSource());
    }

    @Test
    public void getUtmMedium() {
        assertEquals("test2", utmParameters.getUtmMedium());
    }

    @Test
    public void getUtmCampaign() {
        assertEquals("test3", utmParameters.getClass());
    }

    @Test
    public void getUtmTerm() {
        assertEquals("test4", utmParameters.getUtmTerm().get());
    }

    @Test
    public void getUtmContent() {
        assertEquals("test5", utmParameters.getUtmContent().get());
    }

}
