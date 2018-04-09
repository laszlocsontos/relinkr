package com.springuni.hermes.core.convert;

import com.springuni.hermes.click.ClickId;
import com.springuni.hermes.core.orm.EntityClassAwareId;
import org.springframework.core.convert.converter.Converter;

public class LongToEntityClassAwareIdConverterTest<T extends EntityClassAwareId<?>>
        extends AbstractEntityClassAwareIdConverterTest<Long, T> {

    @Override
    public void create_withWrongClass() {
        new LongToEntityClassAwareIdConverter(Object.class);
    }

    @Override
    protected Converter<Long, T> createConverter() {
        return new LongToEntityClassAwareIdConverter(ClickId.class);
    }

    @Override
    protected Long getGoodValue() {
        return 1L;
    }

}
