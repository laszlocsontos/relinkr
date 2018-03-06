package com.springuni.hermes.domain.utm;

import static com.springuni.hermes.domain.Mocks.USER_ID;
import static com.springuni.hermes.domain.Mocks.UTM_PARAMETERS_SET;
import static com.springuni.hermes.domain.Mocks.UTM_TEMPLATE_NAME;

import com.springuni.hermes.domain.core.BaseRepositoryTest;

public class UtmTemplateRepositoryTest extends
        BaseRepositoryTest<UtmTemplate, Long, UtmTemplateRepository> {

    @Override
    protected UtmTemplate createEntity() {
        return new UtmTemplate(UTM_TEMPLATE_NAME, USER_ID, UTM_PARAMETERS_SET);
    }

}
