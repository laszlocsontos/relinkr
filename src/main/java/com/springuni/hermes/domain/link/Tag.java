package com.springuni.hermes.domain.link;

import com.springuni.hermes.domain.core.ValueHolder;
import javax.persistence.Embeddable;

@Embeddable
public class Tag extends ValueHolder<String> {

    public Tag(String value) {
        super(value);
    }

}
