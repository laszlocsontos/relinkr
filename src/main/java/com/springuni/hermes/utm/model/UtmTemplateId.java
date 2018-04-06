package com.springuni.hermes.utm.model;

import com.springuni.hermes.core.orm.AbstractId;

public class UtmTemplateId extends AbstractId<UtmTemplate> {

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    UtmTemplateId() {
    }

    private UtmTemplateId(long id) {
        super(id);
    }

    @Override
    public Class<UtmTemplate> getEntityClass() {
        return UtmTemplate.class;
    }

    public static UtmTemplateId of(long id) {
        return new UtmTemplateId(id);
    }

}
