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

package io.relinkr.test.orm;

import static io.relinkr.test.Mocks.EMAIL_ADDRESS;
import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.relinkr.core.orm.AbstractEntity;
import io.relinkr.core.orm.AbstractId;
import io.relinkr.core.orm.OwnableEntity;
import io.relinkr.core.orm.OwnableRepository;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public abstract class OwnableRepositoryTest<E extends OwnableEntity<ID>, ID extends AbstractId<E>, R extends OwnableRepository<E, ID>>
    extends BaseRepositoryTest<E, ID, R> {

  protected E entity2;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    entity2 = createEntity();
    assertTrue(entity2.isNew());
  }

  @Test
  public void givenNoPageRequest_whenFindByOwner_thenFoundAndOrdered() {
    List<E> savedEntities = saveEntities(entity, entity2);
    List<E> foundEntities = repository.findByOwner(EMAIL_ADDRESS);
    assertEntitiesInOrder(savedEntities, foundEntities);
  }

  @Test
  public void findByUserId_withPageRequest() {
    List<E> savedEntities = saveEntities(entity, entity2);
    Page<E> entityPage = repository.findByOwner(EMAIL_ADDRESS, PageRequest.of(0, 10));
    assertEntitiesInOrder(savedEntities, entityPage.getContent());
  }

  private void assertEntitiesInOrder(List<E> savedEntities, List<E> foundEntities) {
    List<E> expectedEntities = savedEntities.stream()
        .sorted(Comparator.comparing(AbstractEntity::getCreatedDate, reverseOrder()))
        .collect(toList());

    assertEquals(expectedEntities, foundEntities);
  }

  private List<E> saveEntities(E... entities) {
    return Arrays.stream(entities).map(it -> repository.save(it)).collect(toList());
  }

}
