package com.springuni.hermes.domain.click;

import static com.springuni.hermes.domain.Mocks.LINK_ID;
import static com.springuni.hermes.domain.Mocks.TIMESTAMP;
import static com.springuni.hermes.domain.Mocks.VISITOR_ID;
import static com.springuni.hermes.domain.Mocks.VISITOR_IP;

import com.springuni.hermes.core.BaseRepositoryTest;

public class ClickRepositoryTest extends BaseRepositoryTest<Click, Long, ClickRepository> {

    @Override
    protected Click createEntity() {
        return new Click(VISITOR_ID, LINK_ID, VISITOR_IP, TIMESTAMP);
    }

    @Override
    protected Long getNonExistentId() {
        return 0L;
    }

}
