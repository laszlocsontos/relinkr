package com.springuni.hermes.domain.visitor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Visitor extends AbstractPersistable<VisitorId> {

    @Override
    @EmbeddedId
    public VisitorId getId() {
        return super.getId();
    }

}
