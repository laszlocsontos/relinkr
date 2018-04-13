package com.springuni.hermes.core.security.authz;

import static com.springuni.hermes.core.security.authz.MethodSecurityTestController.GET_ENTITY_WITHOUT_ID_METHOD;
import static com.springuni.hermes.core.security.authz.MethodSecurityTestController.GET_ENTITY_WITH_ID_METHOD;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import org.junit.Test;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.MethodSecurityMetadataSource;

public class AuthorizeRolesOrOwnerSecurityMetadataSourceTest {

    private MethodSecurityMetadataSource source = new AuthorizeRolesOrOwnerSecurityMetadataSource();

    @Test
    public void givenMethodWithEntityId_whenGetAttributes_thenRoleAndIsOwnerPresent() {
        Collection<String> attributes = source
                .getAttributes(GET_ENTITY_WITH_ID_METHOD, null)
                .stream()
                .map(ConfigAttribute::getAttribute)
                .collect(toList());

        assertThat(attributes, containsInAnyOrder("IS_OWNER", "ROLE_ADMIN"));
    }

    @Test
    public void givenMethodWithoutEntityId_whenGetAttributes_thenRolePresent() {
        Collection<String> attributes = source
                .getAttributes(GET_ENTITY_WITHOUT_ID_METHOD, null)
                .stream()
                .map(ConfigAttribute::getAttribute)
                .collect(toList());

        assertThat(attributes, allOf(contains("ROLE_ADMIN"), not(contains("IS_OWNER"))));
    }

}
