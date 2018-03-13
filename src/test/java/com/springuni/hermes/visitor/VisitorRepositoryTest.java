package com.springuni.hermes.visitor;

import com.springuni.hermes.core.BaseRepositoryTest;

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
