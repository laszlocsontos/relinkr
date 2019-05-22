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

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import org.junit.Test;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.MethodSecurityMetadataSource;

public class AuthorizeRolesOrOwnerSecurityMetadataSourceTest {

  private MethodSecurityMetadataSource source = new AuthorizeRolesOrOwnerSecurityMetadataSource();

  @Test
  public void givenMethodWithEntityId_whenGetAttributes_thenRoleAndIsOwnerPresent() {
    Collection<String> attributes = source
        .getAttributes(MethodSecurityTestController.GET_ENTITY_WITH_ID_METHOD, null)
        .stream()
        .map(ConfigAttribute::getAttribute)
        .collect(toList());

    assertThat(attributes, containsInAnyOrder("IS_OWNER", "ROLE_ADMIN"));
  }

  @Test
  public void givenMethodWithoutEntityId_whenGetAttributes_thenRolePresent() {
    Collection<String> attributes = source
        .getAttributes(MethodSecurityTestController.GET_ENTITY_WITHOUT_ID_METHOD, null)
        .stream()
        .map(ConfigAttribute::getAttribute)
        .collect(toList());

    assertThat(attributes, allOf(contains("ROLE_ADMIN"), not(contains("IS_OWNER"))));
  }

  @Test
  public void givenMethodWithoutAnnotation_whenGetAttributes_thenNonePresent() {
    Collection<String> attributes = source
        .getAttributes(MethodSecurityTestController.GET_ENTITY_WITHOUT_ANNOTATION_METHOD,
            null)
        .stream()
        .map(ConfigAttribute::getAttribute)
        .collect(toList());

    assertThat(attributes, empty());
  }

}
