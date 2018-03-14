package com.springuni.hermes.link.service;

import com.springuni.hermes.core.orm.OwnableRepository;
import com.springuni.hermes.link.model.Link;
import java.util.Optional;

interface LinkRepository<E extends Link> extends OwnableRepository<E, Long> {

    Optional<E> findByPath(String path);

}
