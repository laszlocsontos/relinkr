package com.springuni.hermes.link.model;

import static lombok.AccessLevel.PROTECTED;

import com.springuni.hermes.core.orm.AbstractId;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
public class LinkSetId extends AbstractId<LinkSet> {

    public LinkSetId(long id) {
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
