package com.springuni.hermes.visitor.model;

import com.springuni.hermes.core.orm.AbstractId;

public class VisitorId extends AbstractId<Visitor> {

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    VisitorId() {
    }

    private VisitorId(long id) {
        super(id);
    }

    @Override
    public Class<Visitor> getEntityClass() {
        return Visitor.class;
    }

    public static VisitorId of(long id) {
        return new VisitorId(id);
    }

}
