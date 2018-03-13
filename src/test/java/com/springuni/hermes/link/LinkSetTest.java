package com.springuni.hermes.link;

import static com.springuni.hermes.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.Mocks.LONG_URL_WITHOUT_UTM_S;
import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_FULL;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_MINIMAL;
import static com.springuni.hermes.Mocks.UTM_TEMPLATE_NAME;
import static org.junit.Assert.assertEquals;

import com.springuni.hermes.utm.UtmTemplate;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;

public class LinkSetTest {

    private LinkSet linkSet;
    private UtmTemplate utmTemplate;

    @Before
    public void setUp() throws Exception {
        utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, USER_ID);
        utmTemplate.addUtmParameters(UTM_PARAMETERS_MINIMAL);
        linkSet = new LinkSet(LONG_URL_BASE_S, utmTemplate, USER_ID);
    }

    @Test
    public void getBaseUrl() throws Exception {
        assertEquals(new URL(LONG_URL_BASE_S), linkSet.getBaseUrl());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getLinks() {
        linkSet.getEmbeddedLinks().add(null);
    }

    @Test
    public void getUtmTemplate() {
        assertEquals(utmTemplate, linkSet.getUtmTemplate());
    }

    @Test
    public void regenerateLinks() {
        linkSet.regenerateLinks();
        assertEquals(1, linkSet.getEmbeddedLinks().size());
    }

    @Test
    public void updateBaseUrl() throws Exception {
        linkSet.updateLongUrl(LONG_URL_WITHOUT_UTM_S);
        assertEquals(new URL(LONG_URL_WITHOUT_UTM_S), linkSet.getBaseUrl());
        assertEquals(new URL(LONG_URL_WITHOUT_UTM_S), linkSet.getEmbeddedLinks().get(0).getBaseUrl());
    }

    @Test
    public void updateUtmTemplate() {
        utmTemplate.addUtmParameters(UTM_PARAMETERS_FULL);
        linkSet.updateUtmTemplate(utmTemplate);
        assertEquals(2, linkSet.getEmbeddedLinks().size());
    }

}
