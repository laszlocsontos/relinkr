package com.springuni.hermes.utm.service;

import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_SET;
import static com.springuni.hermes.Mocks.UTM_TEMPLATE_ID;
import static com.springuni.hermes.Mocks.UTM_TEMPLATE_ID_ZERO;
import static com.springuni.hermes.Mocks.UTM_TEMPLATE_NAME;

import com.springuni.hermes.core.BaseRepositoryTest;
import com.springuni.hermes.utm.model.UtmTemplate;
import com.springuni.hermes.utm.model.UtmTemplateId;

public class UtmTemplateRepositoryTest extends
        BaseRepositoryTest<UtmTemplate, UtmTemplateId, UtmTemplateRepository> {

    @Override
    protected UtmTemplate createEntity() {
        return new UtmTemplate(UTM_TEMPLATE_NAME, USER_ID, UTM_PARAMETERS_SET);
    }

    @Override
    protected UtmTemplateId getId() {
        return UTM_TEMPLATE_ID;
    }

    @Override
    protected UtmTemplateId getNonExistentId() {
        return UTM_TEMPLATE_ID_ZERO;
    }

}
