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

import static org.springframework.util.ReflectionUtils.findMethod;

import io.relinkr.core.security.authz.annotation.AuthorizeRolesOrOwner;
import java.lang.reflect.Method;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MethodSecurityTestController {

  static final Method GET_ENTITY_WITH_ID_METHOD =
      findMethod(MethodSecurityTestController.class, "getEntityWithId", TestId.class);

  static final Method GET_ENTITY_WITHOUT_ANNOTATION_METHOD =
      findMethod(MethodSecurityTestController.class, "getEntityWithoutAnnotation",
          TestId.class);

  static final Method GET_ENTITY_WITHOUT_ID_METHOD =
      findMethod(MethodSecurityTestController.class, "getEntityWithoutId");

  @AuthorizeRolesOrOwner
  @GetMapping("{testId}/with-annotation")
  void getEntityWithId(@PathVariable TestId testId) {

  }

  @GetMapping("{testId}/without-annotation")
  void getEntityWithoutAnnotation(@PathVariable TestId testId) {

  }

  @AuthorizeRolesOrOwner
  void getEntityWithoutId() {

  }

}
