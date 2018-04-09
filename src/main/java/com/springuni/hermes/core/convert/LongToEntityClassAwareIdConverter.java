package com.springuni.hermes.core.convert;

import com.springuni.hermes.core.orm.EntityClassAwareId;
import java.io.Serializable;

public class LongToEntityClassAwareIdConverter<T extends EntityClassAwareId<?>>
        extends AbstractEntityClassAwareIdConverter<Long, T> {

    public LongToEntityClassAwareIdConverter(Class<?> targetClass) {
        super(targetClass);
    }

    @Override
    protected Serializable preProcessSource(Long source) {
        return source;
    }

}
