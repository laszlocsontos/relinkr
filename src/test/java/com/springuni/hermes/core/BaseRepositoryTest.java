package com.springuni.hermes.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public abstract class BaseRepositoryTest<E extends AbstractPersistable<ID>, ID extends Serializable, R extends BaseRepository<E, ID>> {

    @Autowired
    protected R repository;

    protected E entity;

    @Before
    public void setUp() throws Exception {
        entity = createEntity();
        assertTrue(entity.isNew());
    }

    @Test
    public void findById() {
        entity = repository.save(entity);
        Optional<E> entityOptional = repository.findById(entity.getId());
        assertTrue(entityOptional.isPresent());
    }

    @Test
    public void findById_withNonExistent() {
        Optional<E> entityOptional = repository.findById(getNonExistentId());
        assertFalse(entityOptional.isPresent());
    }

    @Test
    public void save() {
        entity = repository.save(entity);
        assertFalse(entity.isNew());
    }

    protected abstract E createEntity() throws Exception;

    protected abstract ID getNonExistentId();

}
