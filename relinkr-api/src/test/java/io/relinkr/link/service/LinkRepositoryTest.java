package io.relinkr.link.service;

import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.createLink;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import io.relinkr.link.model.Link;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class LinkRepositoryTest {

    @Autowired
    private LinkRepository linkRepository;

    private Link link;

    @Before
    public void setUp() throws Exception {
        link = linkRepository.save(createLink());
    }

    @Test
    public void findByUserId() {
        List<Link> links = linkRepository.findByUserId(USER_ID);
        assertThat(links, contains(link));
    }

    @Test
    public void findByUserId_withPageRequest() {
        Page<Link> linkPage = linkRepository.findByUserId(USER_ID, PageRequest.of(0, 10));
        assertEquals(1, linkPage.getTotalElements());
    }

}
