package com.springuni.hermes.domain.link;

import java.util.List;

interface EmbeddedLinkRepository extends LinkRepository<EmbeddedLink> {

    List<EmbeddedLink> findByLinkSetId(long linkSetId);

}
