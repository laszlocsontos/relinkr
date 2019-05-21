package io.relinkr.core.security.authz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * Methods annotated with this annotated authorize those users having any of the roles specified by
 * {@link AuthorizeRolesOrOwner#roles()} or the owner of the resource being access to execute.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AuthorizeRolesOrOwner {

  /**
   * Roles listed here will be authorized to execute the annotated method.
   * <p>Defaults to {@code [ROLE_ADMIN]}.
   */
  @AliasFor("value")
  String[] roles() default {"ROLE_ADMIN"};

  /**
   * Alias for {@link AuthorizeRolesOrOwner#roles()}.
   */
  @AliasFor("roles")
  String[] value() default {"ROLE_ADMIN"};

}
