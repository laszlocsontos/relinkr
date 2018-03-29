package com.springuni.hermes.link.model;

import static com.springuni.hermes.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.Mocks.LONG_URL_WITHOUT_UTM_S;
import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_FULL;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_MINIMAL;
import static com.springuni.hermes.Mocks.UTM_TEMPLATE_NAME;
import static org.junit.Assert.assertEquals;

import com.springuni.hermes.utm.model.UtmTemplate;
import java.net.URI;
import org.junit.Before;
import org.junit.Test;

public class LinkSetTest {

    private LinkSet linkSet;
    private UtmTemplate utmTemplate;

    @Before
    public void setUp() {
        utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, USER_ID);
        utmTemplate.addUtmParameters(UTM_PARAMETERS_MINIMAL);
        linkSet = new LinkSet(LONG_URL_BASE_S, utmTemplate, USER_ID);
    }

    @Test
    public void getLongUrl() {
        assertEquals(URI.create(LONG_URL_BASE_S), linkSet.getLongUrl());
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
    public void updateLongUrl() {
        linkSet.updateLongUrl(LONG_URL_WITHOUT_UTM_S);
        assertEquals(URI.create(LONG_URL_WITHOUT_UTM_S), linkSet.getLongUrl());
        assertEquals(URI.create(LONG_URL_WITHOUT_UTM_S),
                linkSet.getEmbeddedLinks().get(0).getLongUrl());
    }

    @Test
    public void updateUtmTemplate() {
        utmTemplate.addUtmParameters(UTM_PARAMETERS_FULL);
        linkSet.updateUtmTemplate(utmTemplate);
        assertEquals(2, linkSet.getEmbeddedLinks().size());
    }

}
