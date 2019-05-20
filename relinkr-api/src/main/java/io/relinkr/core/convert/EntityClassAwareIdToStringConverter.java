package io.relinkr.core.convert;

import io.relinkr.core.orm.EntityClassAwareId;
import java.util.Optional;
import org.springframework.core.convert.converter.Converter;

public class EntityClassAwareIdToStringConverter<T extends EntityClassAwareId<?>>
        implements Converter<T, String> {

    @Override
    public String convert(T source) {
        return Optional.ofNullable(source).map(String::valueOf).orElse(null);
    }

}
