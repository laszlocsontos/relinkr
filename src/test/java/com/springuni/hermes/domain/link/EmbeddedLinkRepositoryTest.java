package com.springuni.hermes.domain.link;

import static com.springuni.hermes.domain.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.domain.Mocks.USER_ID;
import static com.springuni.hermes.domain.Mocks.UTM_PARAMETERS_FULL;
import static com.springuni.hermes.domain.Mocks.UTM_TEMPLATE_NAME;
import static org.junit.Assert.assertEquals;

import com.springuni.hermes.domain.utm.UtmTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class EmbeddedLinkRepositoryTest {

    @Autowired
    private LinkSetRepository linkSetRepository;

    @Autowired
    private EmbeddedLinkRepository embeddedLinkRepository;

    private LinkSet linkSet;
    private EmbeddedLink embeddedLink;

    @Before
    public void setUp() throws Exception {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, USER_ID);
        utmTemplate.addUtmParameters(UTM_PARAMETERS_FULL);
        LinkSet linkSet = new LinkSet(LONG_URL_BASE_S, utmTemplate, USER_ID);
        linkSet.regenerateLinks();
        this.linkSet = linkSetRepository.save(linkSet);
        embeddedLink = this.linkSet.getEmbeddedLinks().get(0);
    }

    @Test
    public void findByLinkSetId() {
        assertEquals(embeddedLink, embeddedLinkRepository.findByLinkSetId(linkSet.getId()).get(0));
    }

}
