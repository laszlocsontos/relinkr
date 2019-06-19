package io.relinkr;

import static io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver.TOKEN_PAYLOAD_COOKIE_NAME;
import static io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver.TOKEN_SIGNATURE_COOKIE_NAME;
import static io.relinkr.core.web.AjaxRequestMatcher.X_REQUESTED_WITH_HEADER;
import static io.relinkr.core.web.AjaxRequestMatcher.X_REQUESTED_WITH_VALUE;
import static io.relinkr.test.Mocks.EMAIL_ADDRESS;
import static io.relinkr.user.model.UserProfileType.NATIVE;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.relinkr.core.security.authn.jwt.JwtAuthenticationService;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserProfile;
import io.relinkr.user.service.UserService;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("it")
public class RelinkrApiIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtAuthenticationService jwtAuthenticationService;

  @LocalServerPort
  private int testPort;

  private String tokenPayloadCookieValue;
  private String tokenSignatureCookieValue;

  @Before
  public void setUp() {
    User user = userService.saveUser(
        EMAIL_ADDRESS,
        UserProfile.of(NATIVE, EMAIL_ADDRESS.getValue())
    );

    Authentication authentication = new TestingAuthenticationToken(
        String.valueOf(user.getId().getId()),
        null,
        createAuthorityList(user.getAuthorities().toArray(new String[0]))
    );

    String jwtToken = jwtAuthenticationService.createJwtToken(authentication, 1);

    String[] parts = jwtToken.split("\\.");
    tokenPayloadCookieValue = parts[1];
    tokenSignatureCookieValue = parts[0] + "." + parts[2];
  }

  @Test
  public void shouldCreateLink() {
    RequestEntity<?> request = buildRequest(
        POST,
        "/v1/links",
        singletonMap("longUrl", "https://start.spring.io/"),
        emptyMap()
    );

    ResponseEntity<JsonNode> response = testRestTemplate.exchange(request, JsonNode.class);

    assertEquals(OK, response.getStatusCode());
  }

  private RequestEntity<?> buildRequest(
      HttpMethod method, String path, Object body, Map<String, String> params) {

    String cookies = new StringBuilder()
        .append(TOKEN_PAYLOAD_COOKIE_NAME)
        .append("=")
        .append(tokenPayloadCookieValue)
        .append("; ")
        .append(TOKEN_SIGNATURE_COOKIE_NAME)
        .append("=")
        .append(tokenSignatureCookieValue)
        .toString();

    return RequestEntity
        .method(method, buildUri(path, params))
        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .header(COOKIE, cookies)
        .header(X_REQUESTED_WITH_HEADER, X_REQUESTED_WITH_VALUE)
        .body(body);
  }

  private URI buildUri(String path, Map<String, String> params) {
    MultiValueMap<String, String> queryParams = params.entrySet().stream()
        .collect(collectingAndThen(
            toMap(Entry::getKey, it -> singletonList(it.getValue())),
            LinkedMultiValueMap::new)
        );

    UriComponentsBuilder builder = UriComponentsBuilder
        .fromHttpUrl("http://localhost:" + testPort)
        .path(path)
        .queryParams(queryParams);

    return builder.build().toUri();
  }

}
