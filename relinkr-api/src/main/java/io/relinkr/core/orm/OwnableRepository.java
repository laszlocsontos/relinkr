package io.relinkr.core.orm;

import io.relinkr.user.model.Ownable;
import io.relinkr.user.model.UserId;
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
