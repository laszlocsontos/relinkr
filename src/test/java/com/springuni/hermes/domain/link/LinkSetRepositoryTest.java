package com.springuni.hermes.domain.link;

import static com.springuni.hermes.domain.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.domain.Mocks.USER_ID;
import static com.springuni.hermes.domain.Mocks.UTM_PARAMETERS_FULL;
import static com.springuni.hermes.domain.Mocks.UTM_TEMPLATE_NAME;

import com.springuni.hermes.core.BaseRepositoryTest;
import com.springuni.hermes.domain.link.LinkSet;
import com.springuni.hermes.domain.link.LinkSetRepository;
import com.springuni.hermes.domain.utm.UtmTemplate;

public class LinkSetRepositoryTest extends BaseRepositoryTest<LinkSet, Long, LinkSetRepository> {

    @Override
    protected LinkSet createEntity() throws Exception {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, USER_ID);
        utmTemplate.addUtmParameters(UTM_PARAMETERS_FULL);
        LinkSet linkSet = new LinkSet(LONG_URL_BASE_S, utmTemplate, USER_ID);
        linkSet.regenerateLinks();
        return linkSet;
    }

}
