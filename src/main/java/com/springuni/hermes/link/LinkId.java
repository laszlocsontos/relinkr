package com.springuni.hermes.link;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode(of = "value")
public class LinkId implements Serializable {

    private final long value;

    public LinkId(long value) {
        this.value = value;
    }

}
