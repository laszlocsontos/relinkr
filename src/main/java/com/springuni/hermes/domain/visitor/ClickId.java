package com.springuni.hermes.domain.visitor;

import com.springuni.hermes.domain.core.ValueHolder;
import javax.persistence.Embeddable;

@Embeddable
public class ClickId extends ValueHolder<Long> {

    public ClickId(Long value) {
        super(value);
    }

}
