package com.springuni.hermes.link.service;

import com.springuni.hermes.link.model.LinkSet;

public interface LinkSetService extends LinkBaseService<Long, LinkSet> {

    LinkSet addLinkSet(String longUrl, Long utmTemplateId, Long userId);

}
