package com.springuni.hermes.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.springuni.hermes.core.BaseRepositoryTest.TestConfig;
import com.springuni.hermes.core.orm.AbstractEntity;
import com.springuni.hermes.core.orm.BaseRepository;
import com.springuni.hermes.core.orm.JpaConfig;
import com.springuni.hermes.core.orm.UtcLocalDateTimeProvider;
import java.io.Serializable;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
public abstract class BaseRepositoryTest<E extends AbstractEntity<ID>, ID extends Serializable, R extends BaseRepository<E, ID>> {

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
        saveEntity();
        Optional<E> entityOptional = repository.findById(entity.getId());
        assertTrue(entityOptional.isPresent());
    }

    @Test
    public void findById_withNonExistent() {
        Optional<E> entityOptional = repository.findById(getNonExistentId());
        assertFalse(entityOptional.isPresent());
    }

    @Test
    public void save_withAssignedId() {
        ID id = getId();
        entity.setId(id);
        saveEntity();
        assertEquals(id, entity.getId());
        assertFalse(entity.isNew());
    }

    @Test
    public void save_withGeneratedId() {
        entity.setId(null);
        saveEntity();
        assertFalse(entity.isNew());
    }

    @Test
    public void createdDate() {
        saveEntity();
        assertNotNull(entity.getLastModifiedDate());
    }

    @Test
    public void lastModifiedDate() {
        saveEntity();
        assertNotNull(entity.getLastModifiedDate());
    }

    protected abstract E createEntity() throws Exception;

    protected abstract ID getId();

    protected abstract ID getNonExistentId();

    protected void saveEntity() {
        entity = repository.save(entity);
    }

    @TestConfiguration
    @Import(JpaConfig.class)
    static class TestConfig {

        @Bean
        DateTimeProvider utcLocalDateTimeProvider() {
            return new UtcLocalDateTimeProvider();
        }

    }

}
