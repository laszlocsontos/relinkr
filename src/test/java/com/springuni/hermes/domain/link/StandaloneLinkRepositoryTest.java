package com.springuni.hermes.domain.link;

import static com.springuni.hermes.domain.Mocks.LONG_URL_BASE_S;

import com.springuni.hermes.domain.core.BaseRepositoryTest;

public class StandaloneLinkRepositoryTest extends
        BaseRepositoryTest<StandaloneLink, Long, StandaloneLinkRepository> {

    @Override
    protected StandaloneLink createEntity() throws Exception {
        return new StandaloneLink(LONG_URL_BASE_S);
    }

}