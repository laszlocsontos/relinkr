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

package io.relinkr.link.model;

import static lombok.AccessLevel.PROTECTED;

import io.relinkr.core.orm.AbstractId;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
public class LinkId extends AbstractId<Link> {

  public LinkId(long id) {
    super(id);
  }

  public static LinkId of(long id) {
    return new LinkId(id);
  }

}
