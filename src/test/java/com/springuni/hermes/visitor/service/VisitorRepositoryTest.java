package com.springuni.hermes.visitor.service;

import static com.springuni.hermes.Mocks.VISITOR_ID;
import static com.springuni.hermes.Mocks.VISITOR_ID_ZERO;

import com.springuni.hermes.core.BaseRepositoryTest;
import com.springuni.hermes.visitor.model.Visitor;
import com.springuni.hermes.visitor.model.VisitorId;

public class VisitorRepositoryTest extends
        BaseRepositoryTest<Visitor, VisitorId, VisitorRepository> {

    @Override
    protected Visitor createEntity() {
        return new Visitor();
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
