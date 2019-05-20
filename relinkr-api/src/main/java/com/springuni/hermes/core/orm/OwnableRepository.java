package com.springuni.hermes.core.orm;

import com.springuni.hermes.user.model.Ownable;
import com.springuni.hermes.user.model.UserId;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface OwnableRepository<T extends Ownable, ID extends Serializable>
        extends BaseRepository<T, ID> {

    List<T> findByUserId(UserId userId);

    Page<T> findByUserId(UserId userId, Pageable pageable);

}
