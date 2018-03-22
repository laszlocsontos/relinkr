package com.springuni.hermes.core.orm;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.hateoas.Identifiable;

@MappedSuperclass
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class AbstractEntity<ID extends Serializable, E extends AbstractEntity<ID, E>>
        extends AbstractAggregateRoot<E> implements Identifiable<ID>, Persistable<ID>, Serializable {

    @Id
    @GeneratedValue
    private ID id;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Version
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

    void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getVersion() {
        return version;
    }

    void setVersion(Integer version) {
        this.version = version;
    }

}
