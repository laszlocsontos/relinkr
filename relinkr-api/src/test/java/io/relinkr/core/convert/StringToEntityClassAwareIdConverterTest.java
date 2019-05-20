package io.relinkr.core.convert;

import io.relinkr.click.model.ClickId;
import io.relinkr.core.orm.EntityClassAwareId;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

public class StringToEntityClassAwareIdConverterTest<T extends EntityClassAwareId<?>>
        extends AbstractEntityClassAwareIdConverterTest<String, T> {

    @Override
    public void create_withWrongClass() {
        new StringToEntityClassAwareIdConverter(Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void convert_withBadValue() {
        converter.convert("bad");
    }

    @Override
    protected Converter<String, T> createConverter() {
        return new StringToEntityClassAwareIdConverter(ClickId.class);
    }

    @Override
    protected String getGoodValue() {
        return "123";
    }

}