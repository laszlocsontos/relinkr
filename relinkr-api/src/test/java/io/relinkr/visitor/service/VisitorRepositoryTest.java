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
 
package io.relinkr.visitor.service;

import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.VISITOR_ID;
import static io.relinkr.test.Mocks.VISITOR_ID_ZERO;

import io.relinkr.test.orm.BaseRepositoryTest;
import io.relinkr.visitor.model.Visitor;
import io.relinkr.visitor.model.VisitorId;

public class VisitorRepositoryTest extends
    BaseRepositoryTest<Visitor, VisitorId, VisitorRepository> {

  @Override
  protected Visitor createEntity() {
    return Visitor.of(USER_ID);
  }

  @Override
  protected VisitorId getId() {
    return VISITOR_ID;
  }

  @Override
  protected VisitorId getNonExistentId() {
    return VISITOR_ID_ZERO;
  }

}
