package com.springuni.hermes.link.service;

import com.springuni.hermes.link.model.EmbeddedLink;
import com.springuni.hermes.link.model.LinkSetId;
import java.util.List;

interface EmbeddedLinkRepository extends LinkRepository<EmbeddedLink> {

    List<EmbeddedLink> findByLinkSetId(LinkSetId linkSetId);

}
