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

package io.relinkr.user.model;

import static lombok.AccessLevel.PROTECTED;

import io.relinkr.core.orm.AbstractId;
import java.math.BigInteger;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class UserId extends AbstractId<User> {

  private BigInteger id;

  public UserId(BigInteger id) {
    this.id = id;
  }

  public static UserId of(BigInteger id) {
    return new UserId(id);
  }

  @Override
  public Class<User> getEntityClass() {
    return User.class;
  }

  @Override
  public String toString() {
    return Objects.toString(id);
  }

  // FIXME: Remove temporal hack for processing longs

  @Override
  public Long getId() {
    return id.longValue();
  }

  public UserId(long id) {
    this.id = BigInteger.valueOf(id);
  }

  public static UserId of(long id) {
    return new UserId(BigInteger.valueOf(id));
  }

}
