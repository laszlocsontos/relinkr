package com.springuni.hermes.visitor.model;

import static lombok.AccessLevel.PROTECTED;

import com.springuni.hermes.core.orm.AbstractId;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
public class VisitorId extends AbstractId<Visitor> {

    public VisitorId(long id) {
        super(id);
    }

    public static VisitorId of(long id) {
        return new VisitorId(id);
    }

    @Override
    public Class<Visitor> getEntityClass() {
        return Visitor.class;
    }

}
