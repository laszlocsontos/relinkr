package com.springuni.hermes.core.convert;

import com.springuni.hermes.core.orm.EntityClassAwareId;
import java.io.Serializable;
import org.springframework.util.NumberUtils;

public class StringToEntityClassAwareIdConverter<T extends EntityClassAwareId<?>>
        extends AbstractEntityClassAwareIdConverter<String, T> {

    public StringToEntityClassAwareIdConverter(Class<?> targetClass) {
        super(targetClass);
    }

    @Override
    protected Serializable preProcessSource(String source) {
        return NumberUtils.parseNumber(source, Long.class);
    }

}
