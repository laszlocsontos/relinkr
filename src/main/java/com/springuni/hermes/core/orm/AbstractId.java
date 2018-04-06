package com.springuni.hermes.core.orm;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractId<E> implements Serializable {

    private Long id;

    public abstract Class<E> getEntityClass();

}
