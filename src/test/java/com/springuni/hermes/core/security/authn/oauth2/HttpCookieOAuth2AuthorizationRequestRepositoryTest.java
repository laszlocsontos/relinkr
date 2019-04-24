package com.springuni.hermes.core.security.authn.oauth2;

import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.STATE;

import com.springuni.hermes.test.web.BaseServletTest;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@RunWith(MockitoJUnitRunner.class)
public class HttpCookieOAuth2AuthorizationRequestRepositoryTest extends BaseServletTest {

    @Mock
    private OAuth2AuthorizationRequestsCookieResolver authorizationRequestResolver;

    @Captor
    private ArgumentCaptor<Map<String, OAuth2AuthorizationRequest>> authorizationRequestCaptor;

    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

    @Before
    @Override
    public void setUp() {
        super.setUp();

        authorizationRequestRepository =
                new HttpCookieOAuth2AuthorizationRequestRepository(authorizationRequestResolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullHttpServletRequest_whenLoadAuthorizationRequest_thenIllegalArgumentException() {
        this.authorizationRequestRepository.loadAuthorizationRequest(null);
    }

    @Test
    public void givenNoAuthRequestSaved_whenLoadAuthorizationRequest_thenNullReturned() {
        request.addParameter(STATE, "state-1234");
        given(authorizationRequestResolver.resolveRequests(request)).willReturn(Optional.empty());

        OAuth2AuthorizationRequest authorizationRequest =
                this.authorizationRequestRepository.loadAuthorizationRequest(request);

        assertThat(authorizationRequest).isNull();
    }

    @Test
    public void givenSavedAuthRequest_whenLoadAuthorizationRequest_thenAuthRequestReturned() {
        OAuth2AuthorizationRequest authorizationRequest = createAuthorizationRequest().build();
        given(authorizationRequestResolver.resolveRequests(request)).willReturn(
                Optional.of(singletonMap(authorizationRequest.getState(), authorizationRequest))
        );

        request.addParameter(STATE, authorizationRequest.getState());

        OAuth2AuthorizationRequest loadedAuthorizationRequest =
                this.authorizationRequestRepository.loadAuthorizationRequest(request);

        assertThat(loadedAuthorizationRequest).isEqualTo(authorizationRequest);
    }

    @Test
    public void givenMultipleSavedAuthRequest_whenLoadAuthorizationRequest_thenMatchingAuthRequestReturned() {
        String state1 = "state-1122";
        OAuth2AuthorizationRequest authorizationRequest1 =
                createAuthorizationRequest().state(state1).build();

        this.authorizationRequestRepository
                .saveAuthorizationRequest(authorizationRequest1, request, response);

        String state2 = "state-3344";
        OAuth2AuthorizationRequest authorizationRequest2 =
                createAuthorizationRequest().state(state2).build();
        this.authorizationRequestRepository
                .saveAuthorizationRequest(authorizationRequest2, request, response);

        String state3 = "state-5566";
        OAuth2AuthorizationRequest authorizationRequest3 =
                createAuthorizationRequest().state(state3).build();

        Map<String, OAuth2AuthorizationRequest> authorizationRequests =
                unmodifiableMap(new HashMap<String, OAuth2AuthorizationRequest>() {{
                    put(state1, authorizationRequest1);
                    put(state2, authorizationRequest2);
                    put(state3, authorizationRequest3);
                }});

        given(authorizationRequestResolver.resolveRequests(request))
                .willReturn(Optional.of(authorizationRequests));

        request.addParameter(STATE, state1);

        OAuth2AuthorizationRequest loadedAuthorizationRequest1 =
                this.authorizationRequestRepository.loadAuthorizationRequest(request);

        assertThat(loadedAuthorizationRequest1).isEqualTo(authorizationRequest1);

        request.removeParameter(STATE);
        request.addParameter(STATE, state2);

        OAuth2AuthorizationRequest loadedAuthorizationRequest2 =
                this.authorizationRequestRepository.loadAuthorizationRequest(request);

        assertThat(loadedAuthorizationRequest2).isEqualTo(authorizationRequest2);

        request.removeParameter(STATE);
        request.addParameter(STATE, state3);

        OAuth2AuthorizationRequest loadedAuthorizationRequest3 =
                this.authorizationRequestRepository.loadAuthorizationRequest(request);

        assertThat(loadedAuthorizationRequest3).isEqualTo(authorizationRequest3);
    }

    @Test
    public void givenNoStateParam_whenLoadAuthorizationRequest_thenNullReturned() {
        assertThat(authorizationRequestRepository.loadAuthorizationRequest(request)).isNull();
    }

    @Test
    public void givenNullHttpServletRequest_whenSaveAuthorizationRequest_thenIllegalArgumentException() {
        OAuth2AuthorizationRequest authorizationRequest = createAuthorizationRequest().build();

        assertThatThrownBy(() -> this.authorizationRequestRepository.saveAuthorizationRequest(
                authorizationRequest, null, response)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNullHttpServletResponse_whenSaveAuthorizationRequest_thenIllegalArgumentException() {
        OAuth2AuthorizationRequest authorizationRequest = createAuthorizationRequest().build();

        assertThatThrownBy(() -> this.authorizationRequestRepository.saveAuthorizationRequest(
                authorizationRequest, request, null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenAuthRequestWithNullState_whenSaveAuthorizationRequest_thenIllegalArgumentException() {
        OAuth2AuthorizationRequest authorizationRequest = createAuthorizationRequest()
                .state(null)
                .build();

        assertThatThrownBy(() -> this.authorizationRequestRepository.saveAuthorizationRequest(
                authorizationRequest, new MockHttpServletRequest(), new MockHttpServletResponse())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNoAuthRequestSaved_whenSaveAuthorizationRequest_thenIllegalArgumentException() {
        given(authorizationRequestResolver.resolveRequests(request)).willReturn(Optional.empty());

        OAuth2AuthorizationRequest authorizationRequest = createAuthorizationRequest().build();

        authorizationRequestRepository.saveAuthorizationRequest(
                authorizationRequest, request, response
        );

        then(authorizationRequestResolver)
                .should().setRequests(eq(response), authorizationRequestCaptor.capture());

        OAuth2AuthorizationRequest savedAuthorizationRequest =
                authorizationRequestCaptor.getValue().get(authorizationRequest.getState());

        assertThat(savedAuthorizationRequest).isEqualTo(authorizationRequest);
    }

    @Test
    public void givenSavedAuthRequest_whenSaveAuthorizationRequest_thenSaved() {
        OAuth2AuthorizationRequest authorizationRequest1 = createAuthorizationRequest().build();
        given(authorizationRequestResolver.resolveRequests(request)).willReturn(
                Optional.of(singletonMap(authorizationRequest1.getState(), authorizationRequest1))
        );

        OAuth2AuthorizationRequest authorizationRequest2 =
                createAuthorizationRequest().state("state-2345").build();

        this.authorizationRequestRepository.saveAuthorizationRequest(
                authorizationRequest2, request, response);

        then(authorizationRequestResolver)
                .should().setRequests(eq(response), authorizationRequestCaptor.capture());

        Map<String, OAuth2AuthorizationRequest> savedAuthorizationRequests =
                authorizationRequestCaptor.getValue();

        assertThat(savedAuthorizationRequests).contains(
                new SimpleImmutableEntry<>(authorizationRequest1.getState(), authorizationRequest1),
                new SimpleImmutableEntry<>(authorizationRequest2.getState(), authorizationRequest2)
        );
    }

    @Test
    public void givenSavedAuthRequest_withNull_whenSaveAuthorizationRequest_thenRemoved() {
        OAuth2AuthorizationRequest authorizationRequest = createAuthorizationRequest().build();
        given(authorizationRequestResolver.resolveRequests(request)).willReturn(
                Optional.of(singletonMap(authorizationRequest.getState(), authorizationRequest))
        );

        request.addParameter(STATE, authorizationRequest.getState());

        // Null value removes
        authorizationRequestRepository.saveAuthorizationRequest(null, request, response);

        then(authorizationRequestResolver)
                .should().setRequests(response, null);
    }

    @Test
    public void givenNullHttpServletRequest_whenRemoveAuthorizationRequest_thenIllegalArgumentException() {
        assertThatThrownBy(() -> authorizationRequestRepository.removeAuthorizationRequest(
                null, response)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNullHttpServletResponse_whenRemoveAuthorizationRequest_thenIllegalArgumentException() {
        assertThatThrownBy(() -> authorizationRequestRepository.removeAuthorizationRequest(
                request, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenSavedAuthRequest_whenRemoveAuthorization_thenRemoved() {
        OAuth2AuthorizationRequest authorizationRequest = createAuthorizationRequest().build();
        given(authorizationRequestResolver.resolveRequests(request)).willReturn(
                Optional.of(singletonMap(authorizationRequest.getState(), authorizationRequest))
        );

        request.addParameter(STATE, authorizationRequest.getState());

        OAuth2AuthorizationRequest removedAuthorizationRequest =
                authorizationRequestRepository.removeAuthorizationRequest(request, response);

        assertThat(removedAuthorizationRequest).isNotNull();

        then(authorizationRequestResolver).should().setRequests(response, null);
    }

    @Test
    public void givenNoAuthRequestSaved_whenRemoveAuthorizationRequest_thenNotRemoved() {
        given(authorizationRequestResolver.resolveRequests(request)).willReturn(Optional.empty());

        request.addParameter(STATE, "state-1234");

        OAuth2AuthorizationRequest removedAuthorizationRequest =
                authorizationRequestRepository.removeAuthorizationRequest(request, response);

        assertThat(removedAuthorizationRequest).isNull();

        then(authorizationRequestResolver).should(never()).setRequests(eq(response), anyMap());
    }

    private OAuth2AuthorizationRequest.Builder createAuthorizationRequest() {
        return OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri("https://example.com/oauth2/authorize")
                .clientId("client-id-1234")
                .state("state-1234");
    }

}