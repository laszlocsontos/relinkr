package io.relinkr.core.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import io.relinkr.click.model.ClickId;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

public class EntityClassAwareIdToStringConverterTest {

    Converter<ClickId, String> converter;

    @Before
    public void setUp() throws Exception {
        converter = new EntityClassAwareIdToStringConverter<ClickId>();
    }

    @Test
    public void convert_withNull() {
        assertNull(converter.convert(null));
    }

    @Test
    public void convert() {
        assertEquals("123", converter.convert(ClickId.of(123L)));
    }

}
