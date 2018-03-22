package com.springuni.hermes.visitor.service;

import com.springuni.hermes.core.BaseRepositoryTest;
import com.springuni.hermes.visitor.model.Visitor;

public class VisitorRepositoryTest extends BaseRepositoryTest<Visitor, Long, VisitorRepository> {

    @Override
    protected Visitor createEntity() {
        return new Visitor();
    }

    @Override
    protected Long getNonExistentId() {
        return 0L;
    }

}
