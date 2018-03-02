package com.springuni.hermes.domain.link;

import com.springuni.hermes.domain.linkset.LinkSetId;
import java.util.List;

public interface EmbeddedLinkRepository extends LinkRepository<EmbeddedLink> {

    List<EmbeddedLink> listByLinkSetId(LinkSetId linkSetId);

}
