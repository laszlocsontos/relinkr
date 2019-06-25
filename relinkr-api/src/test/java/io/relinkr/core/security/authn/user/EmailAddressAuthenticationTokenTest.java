package io.relinkr.core.security.authn.user;

import static io.relinkr.test.Mocks.EMAIL_ADDRESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import java.util.List;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

public class EmailAddressAuthenticationTokenTest {

  private static final List<GrantedAuthority> AUTHORITIES = createAuthorityList("ROLE_USER");

  private final EmailAddressAuthenticationToken emailAddressAuthenticationToken =
      EmailAddressAuthenticationToken.of(EMAIL_ADDRESS, 1L, AUTHORITIES);

  @Test
  public void shouldReturnEmailAddressAsPrincipal() {
    assertEquals(EMAIL_ADDRESS, emailAddressAuthenticationToken.getPrincipal());
  }

  @Test
  public void shouldReturnEmailAddressAsName() {
    assertEquals(EMAIL_ADDRESS.getValue(), emailAddressAuthenticationToken.getName());
  }

  @Test
  public void shouldReturnNullAsCredentials() {
    assertNull(emailAddressAuthenticationToken.getCredentials());
  }

  @Test
  public void shouldReturnExpiresAtAsDetails() {
    assertEquals(Long.valueOf(1), emailAddressAuthenticationToken.getDetails());
  }

  @Test
  public void shouldBeAuthenticated() {
    assertTrue(emailAddressAuthenticationToken.isAuthenticated());
  }

  @Test
  public void shouldHaveAuthorities() {
    assertEquals(AUTHORITIES, emailAddressAuthenticationToken.getAuthorities());
  }

}
