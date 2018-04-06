package com.springuni.hermes.click;

import com.springuni.hermes.core.orm.AbstractId;

public class ClickId extends AbstractId<Click> {

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    ClickId() {
    }

    private ClickId(long id) {
        super(id);
    }

    @Override
    public Class<Click> getEntityClass() {
        return Click.class;
    }

    public static ClickId of(long id) {
        return new ClickId(id);
    }

}
