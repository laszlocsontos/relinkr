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

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import io.relinkr.core.orm.EntityClassAwareId;
import io.relinkr.core.security.authz.annotation.AuthorizeRolesOrOwner;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.AbstractMethodSecurityMetadataSource;
import org.springframework.util.Assert;

public class AuthorizeRolesOrOwnerSecurityMetadataSource
    extends AbstractMethodSecurityMetadataSource {

  static final String IS_OWNER = "IS_OWNER";
  static final String ROLE_PREFIX = "ROLE_";

  private static final ConfigAttribute IS_OWNER_CONFIG_ATTRIBUTE = new SecurityConfig(IS_OWNER);

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    return null;
  }

  @Override
  public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {
    AuthorizeRolesOrOwner authorizeRolesOrOwner =
        AnnotatedElementUtils.findMergedAnnotation(method, AuthorizeRolesOrOwner.class);

    if (authorizeRolesOrOwner == null) {
      return emptyList();
    }

    Collection<ConfigAttribute> configAttributes = new LinkedList<>();

    configAttributes.addAll(createRoleAttributes(authorizeRolesOrOwner));

    if (hasEntityClassParameter(method)) {
      configAttributes.add(IS_OWNER_CONFIG_ATTRIBUTE);
    }

    return configAttributes;
  }

  private String addRolePrefixIfNeeded(String role) {
    Assert.hasText(role, "role cannot be blank");
    if (role.startsWith(ROLE_PREFIX)) {
      return role;
    }
    return ROLE_PREFIX + role;
  }

  private Collection<ConfigAttribute> createRoleAttributes(
      AuthorizeRolesOrOwner authorizeRolesOrOwner) {

    return Arrays.stream(authorizeRolesOrOwner.roles())
        .map(this::addRolePrefixIfNeeded)
        .map(SecurityConfig::new)
        .collect(toList());
  }

  private boolean hasEntityClassParameter(Method method) {
    return Arrays.stream(method.getParameterTypes())
        .anyMatch(EntityClassAwareId.class::isAssignableFrom);
  }

}
