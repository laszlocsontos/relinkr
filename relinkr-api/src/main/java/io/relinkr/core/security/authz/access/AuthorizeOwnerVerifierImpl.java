package io.relinkr.core.security.authz.access;

import static javax.persistence.LockModeType.NONE;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_ABSTAIN;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_DENIED;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_GRANTED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import io.relinkr.core.orm.EntityClassAwareId;
import io.relinkr.user.model.Ownable;
import io.relinkr.user.model.UserId;
import java.security.Principal;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;

@Slf4j
@Transactional(propagation = SUPPORTS, readOnly = true)
public class AuthorizeOwnerVerifierImpl implements AuthorizeOwnerVerifier {

  private final EntityManager entityManager;

  public AuthorizeOwnerVerifierImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public int canAccess(
      @NonNull Principal principal, @NonNull EntityClassAwareId<?> entityClassAwareId) {

    Class<?> entityClass = entityClassAwareId.getEntityClass();
    if (!Ownable.class.isAssignableFrom(entityClass)) {
      return ACCESS_ABSTAIN;
    }

    Object entity;
    try {
      entity = entityManager.find(entityClass, entityClassAwareId, NONE);
    } catch (PersistenceException e) {
      log.error(e.getMessage(), e);
      return ACCESS_ABSTAIN;
    }

    if (entity == null) {
      return ACCESS_ABSTAIN;
    }

    if (canAccess(principal, (Ownable) entity)) {
      return ACCESS_GRANTED;
    }

    return ACCESS_DENIED;
  }

  private boolean canAccess(Principal principal, Ownable ownable) {
    UserId currentUserId = extractUserId(principal);
    return ownable.getUserId().equals(currentUserId);
  }

  private UserId extractUserId(Principal principal) {
    String name = principal.getName();
    long userId = NumberUtils.parseNumber(name, Long.class);
    return UserId.of(userId);
  }

}
