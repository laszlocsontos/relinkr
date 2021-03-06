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

package io.relinkr.click.model;

import static lombok.AccessLevel.PACKAGE;

import io.relinkr.core.orm.AbstractId;
import javax.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PACKAGE)
public class ClickId extends AbstractId<Click> {

  private static final long serialVersionUID = 9044075687901185668L;

  public ClickId(long id) {
    super(id);
  }

  public static ClickId of(long id) {
    return new ClickId(id);
  }

  @Override
  public Class<Click> getEntityClass() {
    return Click.class;
  }

}
