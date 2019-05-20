package io.relinkr.core.convert;

import static org.springframework.beans.BeanUtils.instantiateClass;

import io.relinkr.core.orm.EntityClassAwareId;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Optional;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

abstract class AbstractEntityClassAwareIdConverter<S extends Serializable, T extends EntityClassAwareId<?>>
        implements Converter<S, T> {

    private final Constructor<T> targetConstructor;

    AbstractEntityClassAwareIdConverter(Class<?> targetClass) {
        checkTargetClass(targetClass);
        targetConstructor = findConstructor((Class<T>) targetClass);
    }

    @Override
    public T convert(S source) {
        if (source == null) {
            return null;
        }

        return instantiateTarget(targetConstructor, preProcessSource(source));
    }

    protected void checkTargetClass(Class<?> targetClass) {
        Assert.isAssignable(EntityClassAwareId.class, targetClass);
    }

    protected Constructor<T> findConstructor(Class<T> targetClass) {
        Constructor<T> constructor = Optional
                .ofNullable(getConstructorIfAvailable(targetClass, long.class))
                .orElseGet(() -> getConstructorIfAvailable(targetClass, Long.class));

        return Optional
                .ofNullable(constructor)
                .orElseThrow(() -> new IllegalArgumentException("No constructor found"));
    }

    protected T instantiateTarget(Constructor<T> targetConstructor, Serializable source) {
        return instantiateClass(targetConstructor, source);
    }

    protected abstract Serializable preProcessSource(S source);

}
