package com.springuni.hermes.domain.linkset;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode(of = "value")
public class LinkSetId implements Serializable {

    private final long value;

    public LinkSetId(long value) {
        this.value = value;
    }

}
