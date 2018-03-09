package com.springuni.hermes.domain.link;

import com.springuni.hermes.core.OwnableRepository;
import java.util.Optional;

interface LinkRepository<E extends Link> extends OwnableRepository<E, Long> {

    Optional<E> findByPath(String path);

}
