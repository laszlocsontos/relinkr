package com.springuni.hermes.domain.link;

import static com.springuni.hermes.domain.link.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.domain.link.Mocks.LONG_URL_WITHOUT_UTM;
import static com.springuni.hermes.domain.link.Mocks.LONG_URL_WITHOUT_UTM_S;
import static com.springuni.hermes.domain.link.Mocks.TAG_A;
import static com.springuni.hermes.domain.link.Mocks.TAG_B;
import static com.springuni.hermes.domain.link.Mocks.UTM_PARAMETERS_MINIMAL;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class StandaloneLinkTest {

    private StandaloneLink link;

    @Before
    public void setUp() throws Exception {
        link = new StandaloneLink(LONG_URL_WITHOUT_UTM);
    }

    @Test
    public void apply() {
        link.apply(UTM_PARAMETERS_MINIMAL);
        assertEquals(UTM_PARAMETERS_MINIMAL, link.longUrl.getUtmParameters());
    }

    @Test
    public void getTags() {
        assertTrue(link.getTags().isEmpty());
    }

    @Test
    public void addTag() {
        link.addTag(TAG_A);
        link.addTag(TAG_B);
        Set<Tag> tags = link.getTags();
        assertThat(tags, contains(TAG_A, TAG_B));
    }

    @Test
    public void removeTag() {
        link.addTag(TAG_A);
        link.addTag(TAG_B);
        link.removeTag(TAG_B);
        Set<Tag> tags = link.getTags();
        assertThat(tags, hasItem(TAG_A));
        assertThat(tags, not(hasItem(TAG_B)));
    }

    @Test
    public void getBaseUrl() {
        assertEquals(LONG_URL_WITHOUT_UTM_S, link.getBaseUrl().toString());
    }

    @Test
    public void getTargetUrl() {
        assertEquals(LONG_URL_WITHOUT_UTM_S, link.getTargetUrl().toString());
    }

    @Test(expected = InvalidLongUrlException.class)
    public void updateLongUrl_withInValidUrl() throws Exception {
        link.updateLongUrl("bad");
    }

    @Test
    public void updateLongUrl_withValidUrl() throws Exception {
        link.updateLongUrl(LONG_URL_BASE_S);
        assertEquals(LONG_URL_BASE_S, link.getBaseUrl().toString());
        assertEquals(LONG_URL_BASE_S, link.getTargetUrl().toString());
    }

}
