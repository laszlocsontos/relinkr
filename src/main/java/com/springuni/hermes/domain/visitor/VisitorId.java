package com.springuni.hermes.domain.visitor;

import com.springuni.hermes.domain.core.ValueHolder;
import javax.persistence.Embeddable;

@Embeddable
public class VisitorId extends ValueHolder<Long> {

    public VisitorId(Long value) {
        super(value);
    }

}
