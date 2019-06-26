package io.relinkr;

import static io.relinkr.core.security.authn.jwt.JwtAuthenticationFilter.X_REQUESTED_WITH_HEADER;
import static io.relinkr.core.security.authn.jwt.JwtAuthenticationFilter.X_REQUESTED_WITH_VALUE;
import static io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver.TOKEN_PAYLOAD_COOKIE_NAME;
import static io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver.TOKEN_SIGNATURE_COOKIE_NAME;
import static io.relinkr.test.Mocks.EMAIL_ADDRESS;
import static io.relinkr.test.Mocks.FIXED_INSTANT;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.fasterxml.jackson.databind.JsonNode;
import io.relinkr.core.security.authn.jwt.JwtAuthenticationService;
import io.relinkr.core.security.authn.user.EmailAddressAuthenticationToken;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integration")
public class RelinkrApiIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private JwtAuthenticationService jwtAuthenticationService;

  @LocalServerPort
  private int testPort;

  @Test
  public void shouldCreateLink() {
    RequestEntity<?> request = buildRequest(
        POST,
        "/v1/links",
        singletonMap("longUrl", "https://start.spring.io/"),
        emptyMap(),
        obtainAuthToken()
    );

    ResponseEntity<JsonNode> response = testRestTemplate.exchange(request, JsonNode.class);

    assertEquals(OK, response.getStatusCode());
  }

  @Test
  public void shouldReportStatusIsUp() {
    RequestEntity<?> request = buildRequest(
        GET,
        "/actuator/health",
        null,
        emptyMap(),
        null
    );

    ResponseEntity<JsonNode> response = testRestTemplate.exchange(request, JsonNode.class);

    assertEquals(OK, response.getStatusCode());
    assertEquals("UP", response.getBody().path("status").asText());
  }

  @Test
  public void shouldRespondWithOK() {
    RequestEntity<?> request = buildRequest(
        GET,
        "/actuator/info",
        null,
        emptyMap(),
        null
    );

    ResponseEntity<JsonNode> response = testRestTemplate.exchange(request, JsonNode.class);

    assertEquals(OK, response.getStatusCode());
  }

  private String obtainAuthToken() {
    Authentication authentication = EmailAddressAuthenticationToken.of(
        EMAIL_ADDRESS,
        FIXED_INSTANT.getEpochSecond(),
        createAuthorityList("ROLE_USER")
    );

    return jwtAuthenticationService.createJwtToken(authentication, 1);
  }

  private RequestEntity<?> buildRequest(
      HttpMethod method, String path, Object body, Map<String, String> params, String jwtToken) {

    BodyBuilder builder = RequestEntity
        .method(method, buildUri(path, params))
        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE);

    if (StringUtils.hasText(jwtToken)) {
      String[] parts = jwtToken.split("\\.");
      String tokenPayloadCookieValue = parts[1];
      String tokenSignatureCookieValue = parts[0] + "." + parts[2];

      String cookies = new StringBuilder()
          .append(TOKEN_PAYLOAD_COOKIE_NAME)
          .append("=")
          .append(tokenPayloadCookieValue)
          .append("; ")
          .append(TOKEN_SIGNATURE_COOKIE_NAME)
          .append("=")
          .append(tokenSignatureCookieValue)
          .toString();

      builder.header(COOKIE, cookies).header(X_REQUESTED_WITH_HEADER, X_REQUESTED_WITH_VALUE);
    }

    if (body != null) {
      return builder.body(body);
    }

    return builder.build();
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
