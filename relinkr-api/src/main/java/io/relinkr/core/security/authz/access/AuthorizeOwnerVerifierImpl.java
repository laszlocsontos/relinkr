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
 
package io.relinkr.core.security.authz.access;

import static javax.persistence.LockModeType.NONE;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_ABSTAIN;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_DENIED;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_GRANTED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import io.relinkr.core.orm.EntityClassAwareId;
import io.relinkr.user.model.Ownable;
import io.relinkr.user.model.UserId;
import java.security.Principal;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;

@Slf4j
@Transactional(propagation = SUPPORTS, readOnly = true)
public class AuthorizeOwnerVerifierImpl implements AuthorizeOwnerVerifier {

  private final EntityManager entityManager;

  public AuthorizeOwnerVerifierImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public int canAccess(
      @NonNull Principal principal, @NonNull EntityClassAwareId<?> entityClassAwareId) {

    Class<?> entityClass = entityClassAwareId.getEntityClass();
    if (!Ownable.class.isAssignableFrom(entityClass)) {
      return ACCESS_ABSTAIN;
    }

    Object entity;
    try {
      entity = entityManager.find(entityClass, entityClassAwareId, NONE);
    } catch (PersistenceException pe) {
      log.error(pe.getMessage(), pe);
      return ACCESS_ABSTAIN;
    }

    if (entity == null) {
      return ACCESS_ABSTAIN;
    }

    if (canAccess(principal, (Ownable) entity)) {
      return ACCESS_GRANTED;
    }

    return ACCESS_DENIED;
  }

  private boolean canAccess(Principal principal, Ownable ownable) {
    UserId currentUserId = extractUserId(principal);
    return ownable.getUserId().equals(currentUserId);
  }

  private UserId extractUserId(Principal principal) {
    String name = principal.getName();
    long userId = NumberUtils.parseNumber(name, Long.class);
    return UserId.of(userId);
  }

}
