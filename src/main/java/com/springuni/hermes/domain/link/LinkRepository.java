package com.springuni.hermes.domain.link;

import com.springuni.hermes.core.OwnableRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
interface LinkRepository<E extends Link> extends OwnableRepository<E, Long> {
}
