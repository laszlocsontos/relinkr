package io.relinkr.core.security.authz.access;

import io.relinkr.core.orm.EntityClassAwareId;
import java.security.Principal;
import org.springframework.aop.framework.AopInfrastructureBean;

public interface AuthorizeOwnerVerifier extends AopInfrastructureBean {

  int canAccess(Principal principal, EntityClassAwareId<?> entityClassAwareId);

}
