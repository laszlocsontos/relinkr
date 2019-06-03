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

import static io.relinkr.core.security.authz.access.AuthorizeRolesOrOwnerSecurityMetadataSource.IS_OWNER;
import static io.relinkr.core.security.authz.access.MethodSecurityTestController.GET_ENTITY_WITH_ID_METHOD;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_ABSTAIN;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_DENIED;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_GRANTED;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.core.Authentication;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizeOwnerVoterTest {

  private static final TestId TEST_ID = TestId.of(1L);

  @Mock
  private Authentication authentication;

  @Mock
  private AuthorizeOwnerVerifier ownerVerifier;

  @Mock
  private MethodInvocation methodInvocation;

  private AccessDecisionVoter<MethodInvocation> voter;

  @Before
  public void setUp() {
    voter = new AuthorizeOwnerVoter(ownerVerifier);
  }

  @Test
  public void givenNullAuthentication_whenVote_thenDenied() {
    int result = voter.vote(null, null, emptyList());
    assertEquals(ACCESS_DENIED, result);
    then(ownerVerifier).shouldHaveZeroInteractions();
  }

  @Test
  public void givenNoEntityClassAwareId_whenVote_thenAbstain() {
    given(methodInvocation.getMethod()).willReturn(GET_ENTITY_WITH_ID_METHOD);
    given(methodInvocation.getArguments()).willReturn(new Object[]{null});

    int result = voter.vote(authentication, methodInvocation, emptyList());

    assertEquals(ACCESS_ABSTAIN, result);
    then(ownerVerifier).shouldHaveZeroInteractions();
  }

  @Test
  public void givenUnsupportedConfigAttribute_whenVote_thenAbstain() {
    given(methodInvocation.getMethod()).willReturn(GET_ENTITY_WITH_ID_METHOD);
    given(methodInvocation.getArguments()).willReturn(new Object[]{null});

    int result = voter
        .vote(authentication, methodInvocation, singletonList(() -> "unsupported"));

    assertEquals(ACCESS_ABSTAIN, result);
    then(ownerVerifier).shouldHaveZeroInteractions();
  }

  @Test
  public void givenSupportedConfigAttribute_withoutId_whenVote_thenAbstain() {
    given(methodInvocation.getMethod()).willReturn(GET_ENTITY_WITH_ID_METHOD);
    given(methodInvocation.getArguments()).willReturn(new Object[]{null});

    int result = voter
        .vote(authentication, methodInvocation, singletonList(() -> IS_OWNER));

    assertEquals(ACCESS_ABSTAIN, result);
    then(ownerVerifier).shouldHaveZeroInteractions();
  }

  @Test
  public void givenSupportedConfigAttribute_withId_whenVote_thenDelegate() {
    given(methodInvocation.getMethod()).willReturn(GET_ENTITY_WITH_ID_METHOD);
    given(methodInvocation.getArguments()).willReturn(new Object[]{TEST_ID});
    given(ownerVerifier.canAccess(authentication, TEST_ID))
        .willReturn(ACCESS_GRANTED);

    int result = voter
        .vote(authentication, methodInvocation, singletonList(() -> IS_OWNER));

    assertEquals(ACCESS_GRANTED, result);
    then(ownerVerifier).should().canAccess(authentication, TEST_ID);
  }

}
