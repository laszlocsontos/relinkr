package com.springuni.hermes.link.service;

import com.springuni.hermes.core.orm.OwnableRepository;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.LinkId;
import java.util.Optional;

interface LinkRepository<E extends Link> extends OwnableRepository<E, LinkId> {

    Optional<E> findByPath(String path);

}
