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

import io.relinkr.core.model.Ownable;
import io.relinkr.core.orm.EntityClassAwareId;
import java.security.Principal;
import org.springframework.aop.framework.AopInfrastructureBean;

/**
 * Used by {@link AuthorizeOwnerVoter} for checking if an entity is owned by a certain user.
 */
public interface AuthorizeOwnerVerifier extends AopInfrastructureBean {

  /**
   * Examines if {@code principal} is the owner of entity identified by {@code entityClassAwareId}.
   *
   * @param principal {@link Principal} which represents the currently authenticated user.
   * @param entityClassAwareId An {@link EntityClassAwareId} representing a resource being accessed.
   * @return Returns {@code ACCESS_ABSTAIN} if the entity isn't
   *        {@link Ownable}, doesn't exists or if there was an error in
   *        accessing that entity; {@code ACCESS_GRANTED} if the current user is the owner;
   *        {@code ACCESS_DENIED} otherwise.
   */
  int canAccess(Principal principal, EntityClassAwareId<?> entityClassAwareId);

}
