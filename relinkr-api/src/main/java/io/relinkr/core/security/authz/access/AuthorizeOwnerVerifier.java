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

import io.relinkr.core.orm.EntityClassAwareId;
import java.security.Principal;
import org.springframework.aop.framework.AopInfrastructureBean;

public interface AuthorizeOwnerVerifier extends AopInfrastructureBean {

  int canAccess(Principal principal, EntityClassAwareId<?> entityClassAwareId);

}
