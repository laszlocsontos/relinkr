package com.springuni.hermes.link.service;

import com.springuni.hermes.link.model.LinkSet;
import com.springuni.hermes.link.model.LinkSetId;
import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.utm.model.UtmTemplate;
import com.springuni.hermes.utm.model.UtmTemplateId;
import com.springuni.hermes.utm.service.UtmTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
class LinkSetServiceImpl
        extends AbstractLinkService<LinkSetId, LinkSet, LinkSetRepository>
        implements LinkSetService {

    private final UtmTemplateService utmTemplateService;

    public LinkSetServiceImpl(
            LinkSetRepository linkSetRepository, UtmTemplateService utmTemplateService) {

        super(linkSetRepository);

        this.utmTemplateService = utmTemplateService;
    }

    @Override
    public LinkSet addLinkSet(String longUrl, UtmTemplateId utmTemplateId, UserId userId) {
        Assert.hasText(longUrl, "longUrl must contain text");
        Assert.notNull(userId, "userId cannot be null");

        UtmTemplate utmTemplate = utmTemplateService.getUtmTemplate(utmTemplateId);
        LinkSet linkSet = new LinkSet(longUrl, utmTemplate, userId);
        return linkRepository.save(linkSet);
    }

}
