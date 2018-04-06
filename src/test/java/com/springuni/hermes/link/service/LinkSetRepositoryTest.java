package com.springuni.hermes.link.service;

import static com.springuni.hermes.Mocks.LINK_SET_ID;
import static com.springuni.hermes.Mocks.LINK_SET_ID_ZERO;
import static com.springuni.hermes.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_FULL;
import static com.springuni.hermes.Mocks.UTM_TEMPLATE_NAME;

import com.springuni.hermes.core.BaseRepositoryTest;
import com.springuni.hermes.link.model.LinkSet;
import com.springuni.hermes.link.model.LinkSetId;
import com.springuni.hermes.utm.model.UtmTemplate;

public class LinkSetRepositoryTest extends
        BaseRepositoryTest<LinkSet, LinkSetId, LinkSetRepository> {

    @Override
    protected LinkSet createEntity() throws Exception {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, USER_ID);
        utmTemplate.addUtmParameters(UTM_PARAMETERS_FULL);
        LinkSet linkSet = new LinkSet(LONG_URL_BASE_S, utmTemplate, USER_ID);
        linkSet.regenerateLinks();
        return linkSet;
    }

    @Override
    protected LinkSetId getId() {
        return LINK_SET_ID;
    }

    @Override
    protected LinkSetId getNonExistentId() {
        return LINK_SET_ID_ZERO;
    }

}
