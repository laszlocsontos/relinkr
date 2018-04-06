package com.springuni.hermes.link.service;

import com.springuni.hermes.core.orm.AbstractId;
import com.springuni.hermes.link.model.LinkBase;
import com.springuni.hermes.user.model.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LinkBaseService<ID extends AbstractId<L>, L extends LinkBase<ID>> {

    L getLink(ID linkId);

    Page<L> listLinks(UserId userId, Pageable pageable);

    void archiveLink(ID linkId);

    void activateLink(ID linkId);

    void addTag(ID linkId, String tagName);

    void removeTag(ID linkId, String tagName);

    L updateLongUrl(ID linkId, String longUrl);

}
