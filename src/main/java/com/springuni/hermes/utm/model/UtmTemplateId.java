package com.springuni.hermes.utm.model;

import static lombok.AccessLevel.PROTECTED;

import com.springuni.hermes.core.orm.AbstractId;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
public class UtmTemplateId extends AbstractId<UtmTemplate> {

    public UtmTemplateId(long id) {
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
