package com.springuni.hermes.domain.user;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode(of = "value")
public class UserId implements Serializable {

    private final long value;

    public UserId(long value) {
        this.value = value;
    }

}
