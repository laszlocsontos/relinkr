package io.relinkr.visitor.service;

import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.VISITOR_ID;
import static io.relinkr.test.Mocks.VISITOR_ID_ZERO;

import io.relinkr.test.orm.BaseRepositoryTest;
import io.relinkr.visitor.model.Visitor;
import io.relinkr.visitor.model.VisitorId;

public class VisitorRepositoryTest extends
        BaseRepositoryTest<Visitor, VisitorId, VisitorRepository> {

    @Override
    protected Visitor createEntity() {
        return Visitor.of(USER_ID);
    }

    @Override
    protected VisitorId getId() {
        return VISITOR_ID;
    }

    @Override
    protected VisitorId getNonExistentId() {
        return VISITOR_ID_ZERO;
    }

}
