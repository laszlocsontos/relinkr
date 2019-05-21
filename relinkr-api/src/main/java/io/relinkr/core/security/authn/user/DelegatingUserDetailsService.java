package io.relinkr.core.security.authn.user;

import io.relinkr.user.model.EmailAddress;
import io.relinkr.user.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by lcsontos on 5/24/17.
 */
@Component
public class DelegatingUserDetailsService implements UserDetailsService {

  private final UserService delegate;

  public DelegatingUserDetailsService(UserService delegate) {
    this.delegate = delegate;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (StringUtils.isEmpty(username)) {
      throw new UsernameNotFoundException("Empty user name");
    }

    EmailAddress emailAddress;
    try {
      emailAddress = EmailAddress.of(username);
    } catch (IllegalArgumentException e) {
      throw new UsernameNotFoundException("Invalid email address", e);
    }

    return delegate.findUser(emailAddress)
        .map(DelegatingUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }

}
