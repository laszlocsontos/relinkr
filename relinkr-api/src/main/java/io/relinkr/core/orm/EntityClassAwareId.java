package io.relinkr.core.orm;

import java.io.Serializable;

public interface EntityClassAwareId<E> extends Serializable {

  Class<E> getEntityClass();

  Long getId();

}
