package com.springuni.hermes.link.model;

import com.springuni.hermes.core.orm.AbstractId;

public class LinkSetId extends AbstractId<LinkSet> {

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    LinkSetId() {
    }

    private LinkSetId(long id) {
        super(id);
    }

    @Override
    public Class<LinkSet> getEntityClass() {
        return LinkSet.class;
    }

    public static LinkSetId of(long id) {
        return new LinkSetId(id);
    }

}
