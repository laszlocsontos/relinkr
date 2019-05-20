package io.relinkr.core.orm;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.Identifiable;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class AbstractEntity<ID extends AbstractId<? extends AbstractEntity<ID>>>
        extends AbstractAggregateRoot implements Identifiable<ID>, Persistable<ID>, Serializable {

    @EmbeddedId
    @GeneratedValue(generator = "time-based")
    @GenericGenerator(
            name = "time-based",
            strategy = "TimeBasedIdentifierGenerator"
    )
    private ID id;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Version
    @Column(name = "version_")
    private Integer version;

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    @Override
    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Integer getVersion() {
        return version;
    }

}
