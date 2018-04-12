package com.springuni.hermes.core.security.access;

import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.USER_ID_ZERO;
import static javax.persistence.LockModeType.NONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_ABSTAIN;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_DENIED;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_GRANTED;

import com.springuni.hermes.core.orm.EntityClassAwareId;
import java.security.Principal;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizeOwnerVerifierTest {

    @Mock
    private Principal principal;

    @Mock
    private EntityClassAwareId entityClassAwareId;

    @Mock
    private EntityManager entityManager;

    private AuthorizeOwnerVerifier ownerVerifier;

    @Before
    public void setUp() throws Exception {
        ownerVerifier = new AuthorizeOwnerVerifierImpl(entityManager);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullPrincipal_whenCanAccess_thenIllegalArgumentException() {
        ownerVerifier.canAccess(null, entityClassAwareId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullEntityClassAwareId_whenCanAccess_thenIllegalArgumentException() {
        ownerVerifier.canAccess(principal, null);
    }

    @Test
    public void givenUnsupportedEntityClass_whenCanAccess_thenAbstain() {
        given(entityClassAwareId.getEntityClass()).willReturn(Object.class);

        int result = ownerVerifier.canAccess(principal, entityClassAwareId);

        assertEquals(ACCESS_ABSTAIN, result);
    }

    @Test
    public void givenNoSuchEntityFound_whenCanAccess_thenAbstain() {
        given(entityClassAwareId.getEntityClass()).willReturn(TestOwnable.class);
        given(entityManager.find(TestOwnable.class, entityClassAwareId, NONE)).willReturn(null);

        int result = ownerVerifier.canAccess(principal, entityClassAwareId);

        assertEquals(ACCESS_ABSTAIN, result);
    }

    @Test
    public void givenEntityOwnedByGivenPrinciple_whenCanAccess_thenGranted() {
        given(principal.getName()).willReturn(USER_ID.toString());
        given(entityClassAwareId.getEntityClass()).willReturn(TestOwnable.class);
        given(entityManager.find(TestOwnable.class, entityClassAwareId, NONE))
                .willReturn(TestOwnable.of(USER_ID));

        int result = ownerVerifier.canAccess(principal, entityClassAwareId);

        assertEquals(ACCESS_GRANTED, result);
    }

    @Test
    public void givenEntityOwnedByGivenPrinciple_andPersistenceError_whenCanAccess_thenAbstain() {
        given(entityClassAwareId.getEntityClass()).willReturn(TestOwnable.class);
        given(entityManager.find(TestOwnable.class, entityClassAwareId, NONE))
                .willThrow(PersistenceException.class);

        int result = ownerVerifier.canAccess(principal, entityClassAwareId);

        assertEquals(ACCESS_ABSTAIN, result);
    }

    @Test
    public void givenEntityOwnedByAnotherPrinciple_whenCanAccess_thenGranted() {
        given(principal.getName()).willReturn(USER_ID_ZERO.toString());
        given(entityClassAwareId.getEntityClass()).willReturn(TestOwnable.class);
        given(entityManager.find(TestOwnable.class, entityClassAwareId, NONE))
                .willReturn(TestOwnable.of(USER_ID));

        int result = ownerVerifier.canAccess(principal, entityClassAwareId);

        assertEquals(ACCESS_DENIED, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenIllegalPrincipalName_whenCanAccess_thenIllegalArgumentException() {
        given(principal.getName()).willReturn("bad");
        given(entityClassAwareId.getEntityClass()).willReturn(TestOwnable.class);
        given(entityManager.find(TestOwnable.class, entityClassAwareId, NONE))
                .willReturn(TestOwnable.of(USER_ID));

        ownerVerifier.canAccess(principal, entityClassAwareId);
    }

}
