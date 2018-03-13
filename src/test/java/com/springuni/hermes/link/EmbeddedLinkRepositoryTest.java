package com.springuni.hermes.link;

import static com.springuni.hermes.Mocks.createLinkSet;
import static org.junit.Assert.assertEquals;

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
        linkSet = linkSetRepository.save(createLinkSet());
        embeddedLink = linkSet.getEmbeddedLinks().get(0);
    }

    @Test
    public void findByLinkSetId() {
        assertEquals(embeddedLink, embeddedLinkRepository.findByLinkSetId(linkSet.getId()).get(0));
    }

}
