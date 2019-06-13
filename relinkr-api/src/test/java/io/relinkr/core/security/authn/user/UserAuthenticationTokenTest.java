package io.relinkr.core.security.authn.user;

import static io.relinkr.test.Mocks.GOOGLE_USER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import java.util.List;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthenticationTokenTest {

  private static final List<GrantedAuthority> AUTHORITIES = createAuthorityList("ROLE_USER");

  private final UserAuthenticationToken userAuthenticationToken =
      UserAuthenticationToken.of(GOOGLE_USER_ID, AUTHORITIES);

  @Test
  public void shouldReturnUserIdAsPrincipal() {
    assertEquals(GOOGLE_USER_ID, userAuthenticationToken.getPrincipal());
  }

  @Test
  public void shouldReturnNullAsCredentials() {
    assertNull(userAuthenticationToken.getCredentials());
  }

  @Test
  public void shouldBeAuthenticated() {
    assertTrue(userAuthenticationToken.isAuthenticated());
  }

  @Test
  public void shouldHaveAuthorities() {
    assertEquals(AUTHORITIES, userAuthenticationToken.getAuthorities());
  }

}
