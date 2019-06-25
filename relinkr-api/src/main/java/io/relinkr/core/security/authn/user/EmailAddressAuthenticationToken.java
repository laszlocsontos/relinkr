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

import io.relinkr.core.model.EmailAddress;
import java.util.Collection;
import lombok.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Represents an authenticated {@link org.springframework.security.oauth2.core.user.OAuth2User}
 * by their user ID.
 */
public class EmailAddressAuthenticationToken extends AbstractAuthenticationToken {

  private final EmailAddress principal;
  private final long expiresAt;

  private EmailAddressAuthenticationToken(
      EmailAddress principal,
      long expiresAt,
      Collection<? extends GrantedAuthority> authorities) {

    super(authorities);
    setAuthenticated(true);

    this.principal = principal;
    this.expiresAt = expiresAt;
  }

  /**
   * Creates a new {@code EmailAddressAuthenticationToken} based on the following data.
   *
   * @param principal User's email address
   * @param expiresAt Seconds since the Epoch
   * @param authorities User's authorities
   * @return a {@code EmailAddressAuthenticationToken} instance
   */
  public static EmailAddressAuthenticationToken of(
      @NonNull EmailAddress principal,
      long expiresAt,
      Collection<? extends GrantedAuthority> authorities) {

    return new EmailAddressAuthenticationToken(principal, expiresAt, authorities);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public EmailAddress getPrincipal() {
    return principal;
  }

  @Override
  public Long getDetails() {
    return expiresAt;
  }

  @Override
  public String getName() {
    return principal.getValue();
  }

}
