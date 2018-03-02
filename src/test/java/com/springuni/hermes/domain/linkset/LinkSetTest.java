package com.springuni.hermes.domain.linkset;

import static com.springuni.hermes.domain.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.domain.Mocks.LONG_URL_WITHOUT_UTM_S;
import static com.springuni.hermes.domain.Mocks.OWNER;
import static com.springuni.hermes.domain.Mocks.UTM_PARAMETERS_FULL;
import static com.springuni.hermes.domain.Mocks.UTM_PARAMETERS_MINIMAL;
import static com.springuni.hermes.domain.Mocks.UTM_TEMPLATE_NAME;
import static org.junit.Assert.assertEquals;

import com.springuni.hermes.domain.utm.UtmTemplate;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;

public class LinkSetTest {

    private LinkSet linkSet;
    private UtmTemplate utmTemplate;

    @Before
    public void setUp() throws Exception {
        utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, OWNER);
        utmTemplate.addUtmParameters(UTM_PARAMETERS_MINIMAL);
        linkSet = new LinkSet(LONG_URL_BASE_S, utmTemplate, OWNER);
    }

    @Test
    public void getBaseUrl() throws Exception {
        assertEquals(new URL(LONG_URL_BASE_S), linkSet.getBaseUrl());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getLinks() {
        linkSet.getLinks().add(null);
    }

    @Test
    public void getUtmTemplate() {
        assertEquals(utmTemplate, linkSet.getUtmTemplate());
    }

    @Test
    public void regenerateLinks() {
        linkSet.regenerateLinks();
        assertEquals(1, linkSet.getLinks().size());
    }

    @Test
    public void updateBaseUrl() throws Exception {
        linkSet.updateBaseUrl(LONG_URL_WITHOUT_UTM_S);
        assertEquals(new URL(LONG_URL_WITHOUT_UTM_S), linkSet.getBaseUrl());
        assertEquals(new URL(LONG_URL_WITHOUT_UTM_S), linkSet.getLinks().get(0).getBaseUrl());
    }

    @Test
    public void updateUtmTemplate() {
        utmTemplate.addUtmParameters(UTM_PARAMETERS_FULL);
        linkSet.updateUtmTemplate(utmTemplate);
        assertEquals(2, linkSet.getLinks().size());
    }

}
