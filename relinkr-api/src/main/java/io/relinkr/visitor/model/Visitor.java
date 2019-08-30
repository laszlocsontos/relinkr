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

package io.relinkr.visitor.model;

import io.relinkr.core.orm.AbstractEntity;
import javax.persistence.Entity;
import lombok.Getter;

/**
 * Represents an anonymous visitor who clicked on a shortened link. We don't wish to record and
 * keep any personal data; the only goal of tracking visitors is to be able to calculate a new vs.
 * returning ratio. A {@code Visitor} is merely an ID with some metadata and no personal data
 * attached.
 */
@Getter
@Entity
public class Visitor extends AbstractEntity<VisitorId> {

  private static final long serialVersionUID = -1273665863437392380L;

  /**
   * Creates a new {@code Visitor} with an empty ID.
   */
  public Visitor() {
  }

}
