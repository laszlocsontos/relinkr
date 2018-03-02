package com.springuni.hermes.domain.link;

import static com.springuni.hermes.domain.link.Mocks.OWNER;
import static com.springuni.hermes.domain.link.Mocks.UTM_PARAMETERS_FULL;
import static com.springuni.hermes.domain.link.Mocks.UTM_PARAMETERS_MINIMAL;
import static com.springuni.hermes.domain.link.Mocks.UTM_PARAMETERS_SET;
import static com.springuni.hermes.domain.link.Mocks.UTM_TEMPLATE_NAME;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UtmTemplateTest {

    @Test
    public void create_withoutUtmParameters() {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, OWNER);
        assertTrue(utmTemplate.getUtmParametersSet().isEmpty());
    }

    @Test
    public void create_withUtmParameters() {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, OWNER, UTM_PARAMETERS_SET);
        assertFalse(utmTemplate.getUtmParametersSet().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getUtmParameters_tryModify() {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, OWNER, UTM_PARAMETERS_SET);
        utmTemplate.getUtmParametersSet().add(UTM_PARAMETERS_MINIMAL);
    }

    @Test
    public void setUtmParameters() {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, OWNER);
        utmTemplate.setUtmParametersSet(UTM_PARAMETERS_SET);
        assertTrue(utmTemplate.getUtmParametersSet().contains(UTM_PARAMETERS_FULL));
        assertTrue(utmTemplate.getUtmParametersSet().contains(UTM_PARAMETERS_MINIMAL));
    }

    @Test
    public void addUtmParameters() {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, OWNER);
        utmTemplate.addUtmParameters(UTM_PARAMETERS_MINIMAL);
        assertThat(utmTemplate.getUtmParametersSet(), hasItem(UTM_PARAMETERS_MINIMAL));
    }

    @Test
    public void removeUtmParameters() {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, OWNER, UTM_PARAMETERS_SET);
        utmTemplate.removeUtmParameters(UTM_PARAMETERS_MINIMAL);
        assertThat(utmTemplate.getUtmParametersSet(), not((hasItem(UTM_PARAMETERS_MINIMAL))));
        assertThat(utmTemplate.getUtmParametersSet(), hasItem(UTM_PARAMETERS_FULL));
    }

}
