package com.springuni.hermes.core.convert;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.springuni.hermes.core.orm.EntityClassAwareId;
import java.io.Serializable;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

public abstract class AbstractEntityClassAwareIdConverterTest<S extends Serializable, T extends EntityClassAwareId<?>> {

    protected Converter<S, T> converter;

    @Before
    public void setUp() throws Exception {
        converter = createConverter();
    }

    @Test(expected = IllegalArgumentException.class)
    public abstract void create_withWrongClass();

    @Test
    public void convert_withNull() {
        assertNull(converter.convert(null));
    }

    @Test
    public void convert_withGoodValue() {
        assertNotNull(converter.convert(getGoodValue()));
    }

    protected abstract Converter<S, T> createConverter();

    protected abstract S getGoodValue();

}
