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
import javax.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class UserId extends AbstractId<User> {

  private static final long serialVersionUID = 1808942031238065852L;

  public UserId(long id) {
    super(id);
  }

  public static UserId of(long id) {
    return new UserId(id);
  }

  @Override
  public Class<User> getEntityClass() {
    return User.class;
  }

}
