package com.springuni.hermes.link.service;

import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.createLinkSet;
import static com.springuni.hermes.Mocks.createStandaloneLink;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import com.springuni.hermes.link.model.EmbeddedLink;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.LinkSet;
import com.springuni.hermes.link.model.StandaloneLink;
import com.springuni.hermes.link.service.LinkRepository;
import com.springuni.hermes.link.service.LinkSetRepository;
import com.springuni.hermes.link.service.StandaloneLinkRepository;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class LinkRepositoryTest {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private LinkSetRepository linkSetRepository;

    @Autowired
    private StandaloneLinkRepository standaloneLinkRepository;

    private EmbeddedLink embeddedLink;
    private StandaloneLink standaloneLink;

    @Before
    public void setUp() throws Exception {
        LinkSet linkSet = linkSetRepository.save(createLinkSet());
        embeddedLink = linkSet.getEmbeddedLinks().get(0);
        standaloneLink = standaloneLinkRepository.save(createStandaloneLink());
    }

    @Test
    public void findByUserId() {
        List<Link> links = linkRepository.findByUserId(USER_ID);
        assertThat(links, contains(embeddedLink, standaloneLink));
    }

}
