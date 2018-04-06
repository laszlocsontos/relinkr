package com.springuni.hermes.link.model;

import com.springuni.hermes.core.orm.AbstractId;

public class LinkId extends AbstractId<Link> {

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    LinkId() {
    }

    private LinkId(long id) {
        super(id);
    }

    @Override
    public Class<Link> getEntityClass() {
        return Link.class;
    }

    public static LinkId of(long id) {
        return new LinkId(id);
    }

}
