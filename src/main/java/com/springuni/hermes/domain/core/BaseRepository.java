package com.springuni.hermes.domain.core;

import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface BaseRepository<E, ID> extends Repository<E, ID> {

    Optional<E> findById(ID id);

    E save(E entity);

}
