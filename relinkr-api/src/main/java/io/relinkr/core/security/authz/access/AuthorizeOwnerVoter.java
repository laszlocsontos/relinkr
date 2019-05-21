package io.relinkr.core.security.authz.access;

import static io.relinkr.core.security.authz.access.AuthorizeRolesOrOwnerSecurityMetadataSource.IS_OWNER;

import io.relinkr.core.orm.EntityClassAwareId;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.IntStream;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

public class AuthorizeOwnerVoter implements AccessDecisionVoter<MethodInvocation> {

  private final AuthorizeOwnerVerifier ownerVerifier;

  public AuthorizeOwnerVoter(AuthorizeOwnerVerifier ownerVerifier) {
    this.ownerVerifier = ownerVerifier;
  }

  @Override
  public boolean supports(ConfigAttribute attribute) {
    return Optional.ofNullable(attribute.getAttribute())
        .map(IS_OWNER::equals)
        .orElse(false);
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return MethodInvocation.class.isAssignableFrom(clazz);
  }

  @Override
  public int vote(
      Authentication authentication, MethodInvocation methodInvocation,
      Collection<ConfigAttribute> attributes) {

    if (authentication == null) {
      return ACCESS_DENIED;
    }

    EntityClassAwareId<?> entityClassAwareId = extractEntityClassAwareId(methodInvocation);
    if (entityClassAwareId == null) {
      return ACCESS_ABSTAIN;
    }

    for (ConfigAttribute attribute : attributes) {
      if (supports(attribute)) {
        return ownerVerifier.canAccess(authentication, entityClassAwareId);
      }
    }

    return ACCESS_ABSTAIN;

  }

  private EntityClassAwareId<?> extractEntityClassAwareId(MethodInvocation methodInvocation) {
    Method method = methodInvocation.getMethod();

    Class<?>[] parameterTypes = method.getParameterTypes();

    int parameterIndex = IntStream.range(0, parameterTypes.length)
        .filter(index -> EntityClassAwareId.class.isAssignableFrom(parameterTypes[index]))
        .findFirst()
        .orElse(-1);

    if (parameterIndex == -1) {
      return null;
    }

    Object[] arguments = methodInvocation.getArguments();
    return (EntityClassAwareId<?>) arguments[parameterIndex];
  }

}
