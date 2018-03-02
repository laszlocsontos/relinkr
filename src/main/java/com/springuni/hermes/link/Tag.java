package com.springuni.hermes.link;

import com.springuni.hermes.core.ValueHolder;
import javax.persistence.Embeddable;

@Embeddable
public class Tag extends ValueHolder<String> {

    public Tag(String value) {
        super(value);
    }

}
