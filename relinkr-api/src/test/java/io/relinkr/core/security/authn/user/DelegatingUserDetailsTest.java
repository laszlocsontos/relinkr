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

package io.relinkr.core.security.authn.user;

import static io.relinkr.test.Mocks.createUser;
import static io.relinkr.user.model.User.ROLE_PREFIX;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import io.relinkr.user.model.User;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DelegatingUserDetailsTest {

  private final User user = createUser();
  private final UserDetails userDetails = DelegatingUserDetails.of(user);

  @Test
  public void shouldHaveSameAuthorities() {
    assertThat(
        userDetails.getAuthorities(),
        containsInAnyOrder(user.getRoles()
            .stream().map(it -> new SimpleGrantedAuthority(ROLE_PREFIX + it.name())).toArray()
        )
    );
  }

  @Test
  public void shouldHaveSamePassword() {
    assertEquals(user.getEncryptedPassword().get(), userDetails.getPassword());
  }

  @Test
  public void shouldHaveSameUsername() {
    assertEquals(String.valueOf(user.getId()), userDetails.getUsername());
  }

  @Test
  public void shouldBeAccountNonExpired() {
    assertTrue(userDetails.isAccountNonExpired());
  }

  @Test
  public void shouldBeNonLocked() {
    assertEquals(!user.isLocked(), userDetails.isAccountNonLocked());
  }

  @Test
  public void shouldBeCredentialsNonExpired() {
    assertTrue(userDetails.isCredentialsNonExpired());
  }

  @Test
  public void shouldBeEnabled() {
    assertEquals(user.isConfirmed(), userDetails.isEnabled());
  }

}
