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

import io.relinkr.user.model.Ownable;
import io.relinkr.user.model.UserId;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import lombok.NonNull;

/**
 * Abstract based class for entities which are owned by a specific user, that is, they have an
 * {@code userId} property.
 *
 * @param <ID> ID type, must be a descendant of {@link AbstractId}.
 */
@MappedSuperclass
public abstract class OwnableEntity<ID extends AbstractId<? extends OwnableEntity<ID>>>
    extends AbstractEntity<ID> implements Ownable {

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "user_id"))
  private UserId userId;

  public OwnableEntity(@NonNull UserId userId) {
    this.userId = userId;
  }

  /*
   * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
   */
  protected OwnableEntity() {
  }


  @Override
  public UserId getUserId() {
    return userId;
  }

}
