package com.springuni.hermes.link.service;

import static com.springuni.hermes.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.Mocks.LONG_URL_VALID_UTM_S;
import static com.springuni.hermes.Mocks.LONG_URL_WITHOUT_UTM_S;
import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.createLinkSet;
import static com.springuni.hermes.Mocks.createUtmTemplate;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import com.springuni.hermes.link.model.LinkSet;
import com.springuni.hermes.utm.model.UtmTemplate;
import com.springuni.hermes.utm.service.UtmTemplateService;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LinkSetServiceTest extends
        AbstractLinkServiceTest<Long, LinkSet, LinkSetRepository, LinkSetService> {

    @Mock
    private LinkSetRepository linkSetRepository;

    @Mock
    private UtmTemplateService utmTemplateService;

    private LinkSet linkSet;
    private LinkSetService linkSetService;
    private UtmTemplate utmTemplate;

    @Before
    public void setUp() throws Exception {
        linkSetService = new LinkSetServiceImpl(linkSetRepository, utmTemplateService);
        linkSet = createLinkSet();
        utmTemplate = createUtmTemplate();
        super.setUp();
    }

    @Override
    protected Long createLinkId() {
        return linkSet.getId();
    }

    @Override
    protected LinkSet createLink() {
        return linkSet;
    }

    @Override
    protected List<LinkSet> createLinkList() {
        return Arrays.asList(linkSet);
    }

    @Override
    protected LinkSetRepository createLinkRepository() {
        return linkSetRepository;
    }

    @Override
    protected LinkSetService createLinkService() {
        return linkSetService;
    }

    @Test
    public void addLinkSet() throws Exception {
        given(utmTemplateService.getUtmTemplate(utmTemplate.getId())).willReturn(utmTemplate);
        linkSetService.addLinkSet(LONG_URL_BASE_S, utmTemplate.getId(), USER_ID);

        LinkSet linkSet = captureSavedLink();
        assertEquals(USER_ID, linkSet.getUserId());
        assertEquals(utmTemplate, linkSet.getUtmTemplate());
        assertEquals(new URL(LONG_URL_BASE_S), linkSet.getLongUrl());
    }

    @Test
    public void updateLinkSet() throws Exception {
        given(linkSetRepository.findById(linkSet.getId())).willReturn(Optional.of(linkSet));
        linkSetService.updateLinkSet(linkId, LONG_URL_VALID_UTM_S);

        LinkSet linkSet = captureSavedLink();
        assertEquals(USER_ID, linkSet.getUserId());
        assertEquals(utmTemplate, linkSet.getUtmTemplate());
        assertEquals(new URL(LONG_URL_WITHOUT_UTM_S), linkSet.getLongUrl());
    }

}
