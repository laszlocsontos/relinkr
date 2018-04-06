package com.springuni.hermes.link.service;

import static com.springuni.hermes.Mocks.LINK_ID;
import static com.springuni.hermes.Mocks.LINK_ID_ZERO;
import static com.springuni.hermes.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.Mocks.USER_ID;
import static org.junit.Assert.assertEquals;

import com.springuni.hermes.core.BaseRepositoryTest;
import com.springuni.hermes.link.model.LinkId;
import com.springuni.hermes.link.model.StandaloneLink;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class StandaloneLinkRepositoryTest extends
        BaseRepositoryTest<StandaloneLink, LinkId, StandaloneLinkRepository> {

    @Test
    public void findByUserId() {
        saveEntity();
        Page<StandaloneLink> linkPage = repository
                .findByUserId(entity.getUserId(), PageRequest.of(0, 10));
        assertEquals(1, linkPage.getTotalElements());
    }

    @Override
    protected StandaloneLink createEntity() throws Exception {
        return new StandaloneLink(LONG_URL_BASE_S, USER_ID);
    }

    @Override
    protected LinkId getId() {
        return LINK_ID;
    }

    @Override
    protected LinkId getNonExistentId() {
        return LINK_ID_ZERO;
    }

}
