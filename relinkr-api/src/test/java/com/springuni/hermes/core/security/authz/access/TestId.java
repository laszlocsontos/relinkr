package com.springuni.hermes.core.security.authz.access;

import com.springuni.hermes.core.orm.AbstractId;

public class TestId extends AbstractId<TestOwnable> {

    public TestId(long id) {
        super(id);
    }

    public static TestId of(long id) {
        return new TestId(id);
    }

    @Override
    public Class<TestOwnable> getEntityClass() {
        return TestOwnable.class;
    }

}
