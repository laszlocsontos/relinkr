package com.springuni.hermes.link;

import static com.springuni.hermes.link.Mocks.OWNER;
import static com.springuni.hermes.link.Mocks.UTM_PARAMETERS_FULL;
import static com.springuni.hermes.link.Mocks.UTM_PARAMETERS_MINIMAL;
import static com.springuni.hermes.link.Mocks.UTM_PARAMETERS_SET;
import static com.springuni.hermes.link.Mocks.UTM_TEMPLATE_NAME;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UtmTemplateTest {

    @Test
    public void create_withoutUtmParameters() {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, OWNER);
        assertTrue(utmTemplate.getUtmParameters().isEmpty());
    }

    @Test
    public void create_withUtmParameters() {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, OWNER, UTM_PARAMETERS_SET);
        assertFalse(utmTemplate.getUtmParameters().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getUtmParameters_tryModify() {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, OWNER, UTM_PARAMETERS_SET);
        utmTemplate.getUtmParameters().add(UTM_PARAMETERS_MINIMAL);
    }

    @Test
    public void setUtmParameters() {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, OWNER);
        utmTemplate.setUtmParameters(UTM_PARAMETERS_SET);
        assertTrue(utmTemplate.getUtmParameters().contains(UTM_PARAMETERS_FULL));
        assertTrue(utmTemplate.getUtmParameters().contains(UTM_PARAMETERS_MINIMAL));
    }

}
