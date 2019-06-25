/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package io.relinkr.test;

import static java.time.Instant.ofEpochMilli;
import static java.time.Instant.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REGISTRATION_ID;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.model.EmailAddress;
import io.relinkr.core.security.authn.WebSecurityConfig;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import io.relinkr.link.model.LongUrl;
import io.relinkr.link.model.Tag;
import io.relinkr.link.model.UtmParameters;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public final class Mocks {

  public static final EmailAddress EMAIL_ADDRESS = EmailAddress.of("test@test.com");

  public static final Instant FIXED_INSTANT = ofEpochSecond(5553091772L);
  public static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, UTC);

  public static final String LONG_URL_BASE_S
      = "https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/";

  public static final String LONG_URL_FRAGMENT_S = "#howto-spring-mvc";
  public static final String LONG_URL_WITHOUT_UTM_S = LONG_URL_BASE_S + LONG_URL_FRAGMENT_S;
  public static final String LONG_URL_VALID_UTM_S =
      LONG_URL_BASE_S + "?utm_source=source&utm_medium=medium&utm_campaign=campaign" +
          LONG_URL_FRAGMENT_S;

  public static final String LONG_URL_VALID_UTM_S_EMPTY_TC =
      LONG_URL_BASE_S + "?utm_source=source&utm_medium=medium&utm_campaign=campaign"
          + "&utm_term&utm_content=" + LONG_URL_FRAGMENT_S;

  public static final String LONG_URL_INVALID_UTM_S =
      LONG_URL_BASE_S + "?utm_source=source" + LONG_URL_FRAGMENT_S;

  public static final LongUrl LONG_URL_BASE;
  public static final LongUrl LONG_URL_WITHOUT_UTM;
  public static final LongUrl LONG_URL_VALID_UTM;
  public static final LongUrl LONG_URL_VALID_UTM_EMPTY_TC;
  public static final LongUrl LONG_URL_INVALID_UTM;

  public static final String FRONTEND_LOGIN_URL = "https://app.relinkr.io/login";
  public static final String NOT_FOUND_URL = "https://relinkr.io/not-found";

  public static final String UTM_SOURCE_V = "source";
  public static final String UTM_MEDIUM_V = "medium";
  public static final String UTM_CAMPAIGN_V = "campaign";
  public static final String UTM_TERM_V = "term";
  public static final String UTM_CONTENT_V = "content";

  public static final UtmParameters UTM_PARAMETERS_MINIMAL;
  public static final UtmParameters UTM_PARAMETERS_FULL;

  public static final Tag TAG_A = new Tag("A");
  public static final Tag TAG_B = new Tag("B");

  public static final LinkId LINK_ID = LinkId.of(1L);

  public static final Pageable PAGEABLE = PageRequest.of(0, 10);

  public static final String JWT_TOKEN_VALID =
      "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiX2F0aCI6IlVTRVIiLCJleHAiOjEzMDQwOTg3O"
          + "DExOCwiaWF0IjoxNTYwODU5Mjk4LCJqdGkiOiIxNTUwMDczNjI4OTQzODQifQ.ACHw2Xkji1auYbG9lem-w-"
          + "EP7Rz-eQw_Y3lVxGZ4n2v9n19T93d1ar8zzCUoBkgZEblRmrQCybW7dCnwnulGtlT6GjnXIE5hY9Ds-Kentg"
          + "bxDcCORCLhnv6d5alwkUsAX-RsykHfuQAfG8DXcSiaVfZ_DeW_2XzTcqu8CtqJBE2wqst8sv1VaZ03MZ5Oq8"
          + "jDF72yPm8P5Oo41oPg1ZJPXrJ_Udij5IFvbAMHh0tubXbZoj3Q2EJEooxUNYcK-CqqDl5xt8VsiIRqL1mP8P"
          + "MauLcdiMkpM_HXvabFLWj-OvS4BdVX0puAebScKfyhF9w89-tP1UK6kFb560pJ5HbXSw";

  public static final String JWT_TOKEN_INVALID =
      "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIzNDU0MzUzMjQ1MzQ1MyIsInN1YiI6IjUzMjQ1"
          + "MzQ1MzQ1MzQ1IiwiZXhwIjoyNTM0MDIyMTQ0MDAsImlhdCI6MTUxNjIzOTAyMiwiYXV0aG9yaXR"
          + "pZXMiOiJVU0VSIn0.B_m-1j9SqmjrcyHwyMMKsxdBi9qLe2akpfZXq4VPG73ppuJXCuB6GPvDvH"
          + "GeMqLQkCA1Al7iBu1oGU7i5QHc5A";

  public static final String JWT_TOKEN_EXPIRED =
      "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI1MzI0NTM0NTM0NTM0NSIsImV4cCI6MTU1NTA1OTkyNywiaWF0Ijox"
          + "NTU1MDU5ODY3LCJqdGkiOiI1OTk4OTQ3NjY5ODAyOCIsImF1dGhvcml0aWVzIjoiVVNFUiJ9.0F"
          + "ItJV2bbpPsdcqa3AbnNFwucK3tQuWQtOTrZ_g4dTdbpv1w0KjrqY6NlusjP7EzkWj_A79YNBnwv"
          + "C8HeAVwrJVY8uGe854iSD-5Rx3eAiGLxMm4zLsKLLwY_8Ewi6J1ki6wl22uClzSJbBLV7Pl6L_O"
          + "MOwz9Br18kdJo4JBY5oiBc1Ybpb5PTOmNI0U6iJyvg2qh9-8KIB4NT_cb-988-j1DImqP0JgUCj"
          + "4t8MG5JXBiGL3pfPEDO0zkW9Rr__jmqPoxPbDcxSQTo5m361MHOmE8BuCsZvoAbf-oY5JaLij5x"
          + "bq0gAONANoBdQzt264uBu9bTRLXxzw2hGT0AqZMg";

  public static final String JWS_VISITOR_COOKIE_SECRET_KEY =
      "MGBDV!Wu*8G$f#CLK8rB!PgLTnSAaQXs";

  public static final String OAUTH2_CLIENT_ID = "1234";
  public static final String OAUTH2_CLIENT_SECRET = "1234";
  public static final String OAUTH2_CLIENT_REG_ID = "google";
  public static final String OAUTH2_STATE = "state";

  public static final String OAUTH2_BASE_URI = "http://localhost";

  public static final String OAUTH2_REDIRECT_URI =
      OAUTH2_BASE_URI + WebSecurityConfig.OAUTH2_LOGIN_PROCESSES_BASE_URI + "/"
          + OAUTH2_CLIENT_REG_ID;

  public static final ClientRegistration OAUTH2_CLIENT_REGISTRATION;

  public static final OAuth2AuthorizationRequest OAUTH2_AUTHORIZATION_REQUEST;

  public static final OAuth2UserRequest OAUTH2_USER_REQUEST_REQUEST;

  public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_SECRET_KEY =
      "MGBDV!Wu*8G345kddLK8rB!PgLTnSAaQXs";

  public static final String JWS_OAUTH2_AUTHORIZATION_REQUEST_COOKIE_VALUE =
      "eyJhbGciOiJIUzI1NiJ9.eyJzdGF0ZSI6eyJhdXRob3JpemF0aW9uVXJpIjoiaHR0cHM6Ly9hY2NvdW50cy5nb"
          + "29nbGUuY29tL28vb2F1dGgyL3YyL2F1dGgiLCJjbGllbnRJZCI6IjEyMzQiLCJyZWRpcmVjdFVya"
          + "SI6Imh0dHA6Ly9sb2NhbGhvc3Qvb2F1dGgyL2xvZ2luL2dvb2dsZSIsInNjb3BlcyI6W10sInN0Y"
          + "XRlIjoic3RhdGUiLCJhZGRpdGlvbmFsUGFyYW1ldGVycyI6eyJyZWdpc3RyYXRpb25faWQiOiJnb"
          + "29nbGUifSwiYXV0aG9yaXphdGlvblJlcXVlc3RVcmkiOiJodHRwczovL2FjY291bnRzLmdvb2dsZ"
          + "S5jb20vby9vYXV0aDIvdjIvYXV0aD9yZXNwb25zZV90eXBlPWNvZGUmY2xpZW50X2lkPTEyMzQmc"
          + "3RhdGU9c3RhdGUmcmVkaXJlY3RfdXJpPWh0dHA6Ly9sb2NhbGhvc3Qvb2F1dGgyL2xvZ2luL2dvb"
          + "2dsZSIsImdyYW50VHlwZS52YWx1ZSI6ImF1dGhvcml6YXRpb25fY29kZSJ9fQ.0z2Q2Td8KILXg8"
          + "hzQWpIQspyJu5bn-Fe0bAlptUxZ0g";

  public static final Map<String, Object> GOOGLE_USER_ATTRIBUTES;

  public static final SimpleGrantedAuthority AUTHORITY_USER =
      new SimpleGrantedAuthority("ROLE_USER");

  static {
    try {
      LONG_URL_BASE = new LongUrl(LONG_URL_BASE_S);
      LONG_URL_WITHOUT_UTM = new LongUrl(LONG_URL_WITHOUT_UTM_S);
      LONG_URL_VALID_UTM = new LongUrl(LONG_URL_VALID_UTM_S);
      LONG_URL_VALID_UTM_EMPTY_TC = new LongUrl(LONG_URL_VALID_UTM_S_EMPTY_TC);
      LONG_URL_INVALID_UTM = new LongUrl(LONG_URL_INVALID_UTM_S);

      UTM_PARAMETERS_FULL = new UtmParameters(
          UTM_SOURCE_V,
          UTM_MEDIUM_V,
          UTM_CAMPAIGN_V,
          UTM_TERM_V,
          UTM_CONTENT_V
      );

      UTM_PARAMETERS_MINIMAL = new UtmParameters(
          UTM_SOURCE_V,
          UTM_MEDIUM_V,
          UTM_CAMPAIGN_V
      );

      Map<String, Object> googleUserAttributes = new HashMap<>();

      googleUserAttributes.put("sub", "12345789");
      googleUserAttributes.put("name", "László Csontos");
      googleUserAttributes.put("given_name", "László");
      googleUserAttributes.put("family_name", "Csontos");
      googleUserAttributes.put("profile", "https://plus.google.com/104401221461109262503");
      googleUserAttributes.put("picture",
          "https://lh3.googleusercontent.com/-7EVTpxqEgj8/AAAAAAAAAAI/AAAAAAAAAAA/Qo9wrOAoxPU/photo.jpg");

      GOOGLE_USER_ATTRIBUTES = unmodifiableMap(googleUserAttributes);

      OAUTH2_CLIENT_REGISTRATION = CommonOAuth2Provider.GOOGLE
          .getBuilder(OAUTH2_CLIENT_REG_ID)
          .authorizationGrantType(AUTHORIZATION_CODE)
          .clientId(OAUTH2_CLIENT_ID)
          .clientSecret(OAUTH2_CLIENT_SECRET)
          .scope("email")
          .build();

      OAUTH2_AUTHORIZATION_REQUEST = OAuth2AuthorizationRequest
          .authorizationCode()
          .authorizationUri(OAUTH2_CLIENT_REGISTRATION.getProviderDetails().getAuthorizationUri())
          .clientId(OAUTH2_CLIENT_ID)
          .state(OAUTH2_STATE)
          .redirectUri(OAUTH2_REDIRECT_URI)
          .additionalParameters(singletonMap(REGISTRATION_ID, OAUTH2_CLIENT_REG_ID))
          .build();

      OAUTH2_USER_REQUEST_REQUEST = new OAuth2UserRequest(
          OAUTH2_CLIENT_REGISTRATION,
          new OAuth2AccessToken(
              BEARER,
              "token",
              ofEpochMilli(0),
              ofEpochMilli(Long.MAX_VALUE)
          )
      );
    } catch (Exception e) {
      // This shouldn't happen, if it does, make test cases fail.
      throw new AssertionError(e);
    }
  }

  private Mocks() {
  }

  public static Link createLink() throws ApplicationException {
    Link link = new Link(LONG_URL_WITHOUT_UTM_S, UTM_PARAMETERS_FULL, EMAIL_ADDRESS);
    link.setId(LINK_ID);
    link.markActive();
    return link;
  }

}
