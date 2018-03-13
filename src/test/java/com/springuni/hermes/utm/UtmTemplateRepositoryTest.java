package com.springuni.hermes.utm;

import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_SET;
import static com.springuni.hermes.Mocks.UTM_TEMPLATE_NAME;

import com.springuni.hermes.core.BaseRepositoryTest;

public class UtmTemplateRepositoryTest extends
        BaseRepositoryTest<UtmTemplate, Long, UtmTemplateRepository> {

    @Override
    protected UtmTemplate createEntity() {
        return new UtmTemplate(UTM_TEMPLATE_NAME, USER_ID, UTM_PARAMETERS_SET);
    }

    @Override
    protected Long getNonExistentId() {
        return 0L;
    }

}
