package com.springuni.hermes.link.service;

import com.springuni.hermes.core.ApplicationException;
import com.springuni.hermes.core.EntityNotFoundException;
import com.springuni.hermes.link.model.LinkBase;
import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LinkBaseService<ID extends Serializable, L extends LinkBase<ID>> {

    L getLink(ID linkId) throws EntityNotFoundException;

    Page<L> listLinks(long userId, Pageable pageable);

    void archiveLink(ID linkId) throws ApplicationException;

    void activateLink(ID linkId) throws ApplicationException;

    void addTag(ID linkId, String tagName) throws ApplicationException;

    void removeTag(ID linkId, String tagName) throws EntityNotFoundException;

}
