package com.springuni.hermes.link.model;

import static lombok.AccessLevel.PROTECTED;

import com.springuni.hermes.core.orm.AbstractId;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
public class LinkId extends AbstractId<Link> {

    public LinkId(long id) {
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
