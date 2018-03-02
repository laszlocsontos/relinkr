package com.springuni.hermes.domain.link;

import com.springuni.hermes.domain.core.OwnableRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface LinkRepository<E extends Link> extends OwnableRepository<E, LinkId> {

}
