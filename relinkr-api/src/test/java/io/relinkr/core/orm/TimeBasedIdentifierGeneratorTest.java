/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package io.relinkr.core.orm;

import static lombok.AccessLevel.PACKAGE;
import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import io.relinkr.core.util.IdGenerator;
import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;
import javax.persistence.Embeddable;
import lombok.NoArgsConstructor;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TimeBasedIdentifierGeneratorTest {

  private static final String TEST_ENTITY_NAME = "test";
  private static final Object TEST_ENTITY = new TestEntity();
  private static final long ID = 1;

  @Mock
  private SharedSessionContractImplementor session;

  @Mock
  private EntityPersister entityPersister;

  @Mock
  private Type type;

  @Mock
  private IdGenerator idGenerator;

  private TimeBasedIdentifierGenerator timeBasedIdentifierGenerator;

  @Before
  public void setUp() {
    timeBasedIdentifierGenerator = new TimeBasedIdentifierGenerator(idGenerator);
  }

  @Test(expected = MappingException.class)
  public void givenWrongIdClass_whenConfigure_thenMappingException() {
    given(type.getReturnedClass()).willReturn(Object.class);

    timeBasedIdentifierGenerator.configure(type, getProperties(TEST_ENTITY_NAME), null);
  }

  @Test(expected = MappingException.class)
  public void givenNoEntityName_whenConfigure_thenMappingException() {
    configureEntity(null, TestEntityId.class);
  }

  @Test
  public void givenAssignedTestEntityId_whenGenerate_thenAssignedIdUsed() {
    assignId(ID, TestEntityId.class);

    Serializable id = timeBasedIdentifierGenerator.generate(session, TEST_ENTITY);
    assertEquals(ID, id);

    then(session).should().getEntityPersister(TEST_ENTITY_NAME, TEST_ENTITY);
    then(idGenerator).should(never()).generate();
  }

  @Test
  public void givenAssignedLongId_whenGenerate_thenAssignedIdUsed() {
    assignId(ID, Long.class);

    Serializable id = timeBasedIdentifierGenerator.generate(session, TEST_ENTITY);
    assertEquals(ID, id);

    then(session).should().getEntityPersister(TEST_ENTITY_NAME, TEST_ENTITY);
    then(idGenerator).should(never()).generate();
  }

  @Test
  public void givenNoTestEntityIdAssigned_whenGenerate_thenIdGenerated() {
    assignNoId(TestEntityId.class);

    Serializable id = timeBasedIdentifierGenerator.generate(session, TEST_ENTITY);
    assertNotNull(id);
    assertNotEquals(ID, id);

    then(session).should().getEntityPersister(TEST_ENTITY_NAME, TEST_ENTITY);
    then(idGenerator).should().generate();
  }

  @Test
  public void givenNoLongIdAssigned_whenGenerate_thenIdGenerated() {
    assignNoId(Long.class);

    Serializable id = timeBasedIdentifierGenerator.generate(session, TEST_ENTITY);
    assertNotNull(id);
    assertNotEquals(ID, id);

    then(session).should().getEntityPersister(TEST_ENTITY_NAME, TEST_ENTITY);
    then(idGenerator).should().generate();
  }

  private void configureEntity(String entityName, Class<?> entityIdClass) {
    Properties properties = getProperties(entityName);

    given(type.getReturnedClass()).willReturn(entityIdClass);

    timeBasedIdentifierGenerator.configure(type, properties, null);
  }

  private void assignId(Long idValue, Class<?> idClass) {
    configureEntity(TEST_ENTITY_NAME, idClass);
    given(session.getEntityPersister(TEST_ENTITY_NAME, TEST_ENTITY)).willReturn(entityPersister);
    given(entityPersister.getIdentifier(TEST_ENTITY, session)).willReturn(idValue);
  }

  private void assignNoId(Class<?> idClass) {
    assignId(null, idClass);
  }

  private Properties getProperties(String entityName) {
    Properties properties = new Properties();

    Optional.ofNullable(entityName)
        .ifPresent(it -> properties.put(ENTITY_NAME, entityName));

    return properties;
  }

  private static class TestEntity extends AbstractEntity<TestEntityId> {

  }

  @Embeddable
  @NoArgsConstructor(access = PACKAGE)
  private static class TestEntityId extends AbstractId<TestEntity> {

    public TestEntityId(long id) {
      super(id);
    }

  }


}
