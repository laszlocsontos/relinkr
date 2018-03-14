package com.springuni.hermes.core.web;

import com.springuni.hermes.core.orm.AbstractEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractResource extends ResourceSupport {

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Integer version;

    public AbstractResource(AbstractEntity entity) {
        this.createdDate = entity.getCreatedDate();
        this.lastModifiedDate = entity.getLastModifiedDate();
        this.version = entity.getVersion();
    }

}
