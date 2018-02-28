package com.springuni.hermes.user;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class UserId implements Serializable {

    private final long value;

    public UserId(long value) {
        this.value = value;
    }

}
