package com.springuni.hermes.domain.core;

import com.springuni.hermes.domain.user.Ownable;
import com.springuni.hermes.domain.user.UserId;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface OwnableRepository<T extends Ownable, ID extends Serializable>
        extends BaseRepository<T, ID> {

    List<T> listByOwner(UserId userId);

    Page<T> pageByOwner(UserId userId, Pageable pageable);

}
