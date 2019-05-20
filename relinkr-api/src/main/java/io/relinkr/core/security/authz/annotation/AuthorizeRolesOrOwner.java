package io.relinkr.core.security.authz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AuthorizeRolesOrOwner {

    @AliasFor("value")
    String[] roles() default {"ROLE_ADMIN"};

    @AliasFor("roles")
    String[] value() default {"ROLE_ADMIN"};

}
