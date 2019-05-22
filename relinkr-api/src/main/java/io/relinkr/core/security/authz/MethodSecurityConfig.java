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

package io.relinkr.core.security.authz;

import static java.util.Collections.singletonList;

import io.relinkr.core.security.authz.access.AuthorizeOwnerVerifier;
import io.relinkr.core.security.authz.access.AuthorizeOwnerVerifierImpl;
import io.relinkr.core.security.authz.access.AuthorizeOwnerVoter;
import io.relinkr.core.security.authz.access.AuthorizeRolesOrOwnerSecurityMetadataSource;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.method.DelegatingMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.ConsensusBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

  private final EntityManager entityManager;

  public MethodSecurityConfig(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Bean
  AuthorizeOwnerVerifier authorizeOwnerVerifier() {
    return new AuthorizeOwnerVerifierImpl(entityManager);
  }

  @Bean
  AuthorizeOwnerVoter authorizeOwnerVoter() {
    return new AuthorizeOwnerVoter(authorizeOwnerVerifier());
  }

  @Override
  protected AccessDecisionManager accessDecisionManager() {
    List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
    decisionVoters.add(new RoleVoter());
    decisionVoters.add(authorizeOwnerVoter());
    return new ConsensusBased(decisionVoters);
  }

  @Bean
  @Override
  public MethodSecurityMetadataSource methodSecurityMetadataSource() {
    List<MethodSecurityMetadataSource> sources =
        singletonList(new AuthorizeRolesOrOwnerSecurityMetadataSource());

    return new DelegatingMethodSecurityMetadataSource(sources);
  }

}
