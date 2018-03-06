package com.springuni.hermes.domain.core;

import com.springuni.hermes.domain.user.Ownable;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface OwnableRepository<T extends Ownable, ID extends Serializable>
        extends BaseRepository<T, ID> {

    List<T> findByUserId(long userId);

    Page<T> findByUserId(long userId, Pageable pageable);

}
