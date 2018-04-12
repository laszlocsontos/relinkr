package com.springuni.hermes.core.security.access;

import com.springuni.hermes.core.orm.EntityClassAwareId;
import java.security.Principal;
import org.springframework.aop.framework.AopInfrastructureBean;

public interface AuthorizeOwnerVerifier extends AopInfrastructureBean {

    int canAccess(Principal principal, EntityClassAwareId<?> entityClassAwareId);

}
