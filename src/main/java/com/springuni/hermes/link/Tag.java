package com.springuni.hermes.link;

import javax.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode
public class Tag {

    private final String value;

    public Tag(String value) {
        this.value = value;
    }

}
