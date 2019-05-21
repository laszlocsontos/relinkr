package io.relinkr.test.orm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.relinkr.core.orm.AbstractEntity;
import io.relinkr.core.orm.AbstractId;
import io.relinkr.core.orm.BaseRepository;
import io.relinkr.core.orm.JpaConfig;
import io.relinkr.core.orm.UtcLocalDateTimeProvider;
import io.relinkr.test.orm.BaseRepositoryTest.TestConfig;
import java.util.Optional;
import javax.persistence.EntityManager;
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
public abstract class BaseRepositoryTest<E extends AbstractEntity<ID>, ID extends AbstractId<? extends AbstractEntity<ID>>, R extends BaseRepository<E, ID>> {

  @Autowired
  protected R repository;

  protected E entity;

  @Autowired
  private EntityManager entityManager;

  @Before
  public void setUp() throws Exception {
    entity = createEntity();
    assertTrue(entity.isNew());
  }

  @Test
  public void givenSavedEntity_whenFindById_thenFound() {
    saveEntity();
    Optional<E> entityOptional = repository.findById(entity.getId());
    assertTrue(entityOptional.isPresent());
  }

  @Test
  public void givenNonExistentId_whenFindById_thenNotFound() {
    Optional<E> entityOptional = repository.findById(getNonExistentId());
    assertFalse(entityOptional.isPresent());
  }

  @Test
  public void givenEntityWithAssignedId_whenSave_thenAssignedIdUsed() {
    ID id = getId();
    entity.setId(id);
    saveEntity();
    assertEquals(id, entity.getId());
    assertFalse(entity.isNew());
  }

  @Test
  public void givenEntityWithoutId_whenSave_thenIdGenerated() {
    entity.setId(null);
    saveEntity();
    assertFalse(entity.isNew());
  }

  @Test
  public void givenNewEntity_whenSave_thenCreatedDateSet() {
    saveEntity();
    assertNotNull(entity.getLastModifiedDate());
  }

  @Test
  public void givenNewEntity_whenSave_thenLastModifiedDateSet() {
    saveEntity();
    assertNotNull(entity.getLastModifiedDate());
  }

  protected abstract E createEntity() throws Exception;

  protected abstract ID getId();

  protected abstract ID getNonExistentId();

  protected void saveEntity() {
    entity = repository.save(entity);
    entityManager.flush();
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
