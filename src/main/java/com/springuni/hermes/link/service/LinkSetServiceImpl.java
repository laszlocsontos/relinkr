package com.springuni.hermes.link.service;

import com.springuni.hermes.link.model.LinkSet;
import com.springuni.hermes.utm.model.UtmTemplate;
import com.springuni.hermes.utm.service.UtmTemplateService;
import org.springframework.stereotype.Service;

@Service
class LinkSetServiceImpl
        extends AbstractLinkService<Long, LinkSet, LinkSetRepository> implements LinkSetService {

    private final UtmTemplateService utmTemplateService;

    public LinkSetServiceImpl(
            LinkSetRepository linkSetRepository, UtmTemplateService utmTemplateService) {

        super(linkSetRepository);

        this.utmTemplateService = utmTemplateService;
    }

    @Override
    public LinkSet addLinkSet(String longUrl, Long utmTemplateId, Long userId) {
        UtmTemplate utmTemplate = utmTemplateService.getUtmTemplate(utmTemplateId);
        LinkSet linkSet = new LinkSet(longUrl, utmTemplate, userId);
        return linkRepository.save(linkSet);
    }

    @Override
    public LinkSet updateLinkSet(Long linkSetId, String longUrl) {
        LinkSet linkSet = getLink(linkSetId);
        linkSet.updateLongUrl(longUrl);
        return linkRepository.save(linkSet);
    }

}
