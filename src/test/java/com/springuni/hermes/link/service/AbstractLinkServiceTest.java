package com.springuni.hermes.link.service;

import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.link.model.LinkStatus.ACTIVE;
import static com.springuni.hermes.link.model.LinkStatus.ARCHIVED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.data.domain.Pageable.unpaged;

import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.core.model.EntityNotFoundException;
import com.springuni.hermes.core.orm.AbstractId;
import com.springuni.hermes.core.orm.OwnableRepository;
import com.springuni.hermes.link.model.LinkBase;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public abstract class AbstractLinkServiceTest<ID extends AbstractId<L>, L extends LinkBase<ID>, R extends OwnableRepository<L, ID>, S extends LinkBaseService<ID, L>> {

    protected ID linkId;
    protected L link;
    protected Class<L> linkClass;
    protected List<L> links;
    protected R linkRepository;
    protected S linkService;

    @Before
    public void setUp() throws Exception {
        linkId = createLinkId();
        link = createLink();
        linkClass = (Class<L>) link.getClass();
        links = createLinkList();
        linkRepository = createLinkRepository();
        linkService = createLinkService();
    }

    protected abstract ID createLinkId();

    protected abstract L createLink();

    protected abstract List<L> createLinkList();

    protected abstract R createLinkRepository();

    protected abstract S createLinkService();

    @Test
    public void getLink() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
        L link = linkService.getLink(linkId);
        assertSame(this.link, link);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getLink_withNonExistent() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.empty());
        linkService.getLink(linkId);
    }

    @Test
    public void listLinks() {
        Page<L> linkPage = new PageImpl<>(links);
        given(linkRepository.findByUserId(USER_ID, unpaged())).willReturn(linkPage);
        assertEquals(linkPage, linkService.listLinks(USER_ID, unpaged()));
    }

    @Test
    public void activateLink() throws ApplicationException {
        link.markArchived();
        given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
        linkService.activateLink(linkId);

        L link = captureSavedLink();
        assertEquals(ACTIVE, link.getLinkStatus());
    }

    @Test(expected = EntityNotFoundException.class)
    public void activateLink_withNonExistent() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.empty());
        linkService.activateLink(linkId);
    }

    @Test
    public void archiveLink() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
        linkService.archiveLink(linkId);

        L link = captureSavedLink();
        assertEquals(ARCHIVED, link.getLinkStatus());
    }

    @Test(expected = EntityNotFoundException.class)
    public void archiveLink_withNonExistent() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.empty());
        linkService.archiveLink(linkId);
    }

    @Test
    public void addTag() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
        linkService.addTag(linkId, "test");
    }

    @Test(expected = EntityNotFoundException.class)
    public void addTag_withNonExistent() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.empty());
        linkService.addTag(linkId, "test");
    }

    @Test
    public void removeTag() {
        given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
        linkService.removeTag(linkId, "test");
    }

    @Test(expected = EntityNotFoundException.class)
    public void removeTag_withNonExistent() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.empty());
        linkService.removeTag(linkId, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateLongUrl_withNull() throws ApplicationException {
        linkService.updateLongUrl(linkId, null);
    }

    protected L captureSavedLink() {
        ArgumentCaptor<L> standaloneLinkCaptor = ArgumentCaptor.forClass(linkClass);
        then(linkRepository).should().save(standaloneLinkCaptor.capture());
        return standaloneLinkCaptor.getValue();
    }

}
