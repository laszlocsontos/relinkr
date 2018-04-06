package com.springuni.hermes.click;

import static com.springuni.hermes.Mocks.CLICK_ID;
import static com.springuni.hermes.Mocks.CLICK_ID_ZERO;
import static com.springuni.hermes.Mocks.LINK_ID;
import static com.springuni.hermes.Mocks.TIMESTAMP;
import static com.springuni.hermes.Mocks.VISITOR_ID;
import static com.springuni.hermes.Mocks.VISITOR_IP;

import com.springuni.hermes.core.BaseRepositoryTest;

public class ClickRepositoryTest extends BaseRepositoryTest<Click, ClickId, ClickRepository> {

    @Override
    protected Click createEntity() {
        return new Click(VISITOR_ID, LINK_ID, VISITOR_IP, TIMESTAMP);
    }

    @Override
    protected ClickId getId() {
        return CLICK_ID;
    }

    @Override
    protected ClickId getNonExistentId() {
        return CLICK_ID_ZERO;
    }

}
