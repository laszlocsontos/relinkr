package com.springuni.hermes.click.service;

import static com.springuni.hermes.test.Mocks.CLICK_ID;
import static com.springuni.hermes.test.Mocks.CLICK_ID_ZERO;
import static com.springuni.hermes.test.Mocks.LINK_ID;
import static com.springuni.hermes.test.Mocks.TIMESTAMP;
import static com.springuni.hermes.test.Mocks.USER_ID;
import static com.springuni.hermes.test.Mocks.VISITOR_ID;
import static com.springuni.hermes.test.Mocks.VISITOR_IP;

import com.springuni.hermes.click.model.Click;
import com.springuni.hermes.click.model.ClickId;
import com.springuni.hermes.test.orm.BaseRepositoryTest;

public class ClickRepositoryTest extends BaseRepositoryTest<Click, ClickId, ClickRepository> {

    @Override
    protected Click createEntity() {
        return Click.of(LINK_ID, VISITOR_ID, USER_ID, VISITOR_IP, TIMESTAMP);
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
