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
 
package io.relinkr.click.service;

import static io.relinkr.test.Mocks.CLICK_ID;
import static io.relinkr.test.Mocks.CLICK_ID_ZERO;
import static io.relinkr.test.Mocks.LINK_ID;
import static io.relinkr.test.Mocks.TIMESTAMP;
import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.VISITOR_ID;
import static io.relinkr.test.Mocks.VISITOR_IP;

import io.relinkr.click.model.Click;
import io.relinkr.click.model.ClickId;
import io.relinkr.test.orm.BaseRepositoryTest;

public class ClickRepositoryTest extends BaseRepositoryTest<Click, ClickId, ClickRepository> {

  @Override
  protected Click createEntity() {
    return Click.of(LINK_ID, VISITOR_ID, USER_ID, VISITOR_IP, TIMESTAMP);
  }

  @Override
  protected ClickId getId() {
    return CLICK_ID;
  }

  @Override
  protected ClickId getNonExistentId() {
    return CLICK_ID_ZERO;
  }

}
