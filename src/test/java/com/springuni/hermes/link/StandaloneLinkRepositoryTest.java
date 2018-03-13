package com.springuni.hermes.link;

import static com.springuni.hermes.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.Mocks.USER_ID;

import com.springuni.hermes.core.BaseRepositoryTest;

public class StandaloneLinkRepositoryTest extends
        BaseRepositoryTest<StandaloneLink, Long, StandaloneLinkRepository> {

    @Override
    protected StandaloneLink createEntity() throws Exception {
        return new StandaloneLink(LONG_URL_BASE_S, USER_ID);
    }

    @Override
    protected Long getNonExistentId() {
        return 0L;
    }

}
