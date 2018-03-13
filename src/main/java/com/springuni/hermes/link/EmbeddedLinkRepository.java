package com.springuni.hermes.link;

import java.util.List;

interface EmbeddedLinkRepository extends LinkRepository<EmbeddedLink> {

    List<EmbeddedLink> findByLinkSetId(long linkSetId);

}
