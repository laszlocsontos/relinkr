package com.springuni.hermes.domain.utm;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode(of = "value")
public class UtmTemplateId implements Serializable {

    private final long value;

    public UtmTemplateId(long value) {
        this.value = value;
    }

}
