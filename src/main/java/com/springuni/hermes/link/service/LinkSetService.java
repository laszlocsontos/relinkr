package com.springuni.hermes.link.service;

import com.springuni.hermes.link.model.LinkSet;
import com.springuni.hermes.link.model.LinkSetId;
import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.utm.model.UtmTemplateId;

public interface LinkSetService extends LinkBaseService<LinkSetId, LinkSet> {

    LinkSet addLinkSet(String longUrl, UtmTemplateId utmTemplateId, UserId userId);

}
