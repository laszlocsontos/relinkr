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

package io.relinkr.link.service;

import static io.relinkr.test.Mocks.LINK_ID;
import static io.relinkr.test.Mocks.LONG_URL_WITHOUT_UTM_S;
import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.UTM_PARAMETERS_FULL;
import static org.junit.Assert.assertTrue;

import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import io.relinkr.test.orm.OwnableRepositoryTest;
import java.net.URI;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class LinkRepositoryTest extends OwnableRepositoryTest<Link, LinkId, LinkRepository> {

  @PersistenceContext
  private EntityManager entityManager;

  @Test
  public void givenSavedEntity_whenFindById_thenTargetUrlHasValue() {
    Link savedEntity = saveEntity(this.entity);
    entityManager.refresh(savedEntity);
    Optional<URI> entityOptional = Optional.of(savedEntity).map(Link::getTargetUrl);
    assertTrue(entityOptional.isPresent());
  }

  @Override
  protected Link createEntity() {
    return Link.of(USER_ID, LONG_URL_WITHOUT_UTM_S, UTM_PARAMETERS_FULL);
  }

  @Override
  protected LinkId getId() {
    return LinkId.of(idGenerator.generate());
  }

  @Override
  protected LinkId getNonExistentId() {
    return LINK_ID;
  }

}
