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

package io.relinkr.link.web;

import static org.springframework.http.HttpHeaders.readOnlyHttpHeaders;
import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.model.EntityNotFoundException;
import io.relinkr.link.model.Link;
import io.relinkr.link.service.LinkService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * Entry point for resolving short links to their target URLs.
 */
@Controller
public class RedirectController {

  static final String FRONT_END_LOGIN_URL_PROPERTY = "relinkr.frontend.login-url";
  static final String REDIRECT_NOT_FOUND_URL_PROPERTY = "relinkr.redirect.not-found-url";

  static final String HEADER_XFF = "X-Forwarded-For";

  private static final HttpHeaders HTTP_HEADERS;

  static {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setCacheControl("no-cache, no-store, max-age=0, must-revalidate");
    httpHeaders.setExpires(0);
    httpHeaders.setPragma("no-cache");

    HTTP_HEADERS = readOnlyHttpHeaders(httpHeaders);
  }

  private final URI frontendLoginUrl;
  private final URI notFoundUrl;
  private final LinkService linkService;

  @Autowired
  public RedirectController(Environment environment, LinkService linkService) {
    frontendLoginUrl = environment.getRequiredProperty(FRONT_END_LOGIN_URL_PROPERTY, URI.class);
    notFoundUrl = environment.getRequiredProperty(REDIRECT_NOT_FOUND_URL_PROPERTY, URI.class);
    this.linkService = linkService;
  }

  @GetMapping("/")
  ResponseEntity handleRoot() {
    return buildRedirect(frontendLoginUrl);
  }

  @GetMapping("/{path}")
  ResponseEntity redirectLink(@PathVariable(required = false) String path)
      throws ApplicationException {

    if (StringUtils.isEmpty(path)) {
      return buildRedirect(notFoundUrl);
    }

    Link link;
    try {
      link = linkService.getLink(path);
    } catch (EntityNotFoundException enfe) {
      return buildRedirect(notFoundUrl);
    }

    return buildRedirect(link.getTargetUrl());
  }

  private ResponseEntity buildRedirect(URI targetUrl) {
    return ResponseEntity
        .status(MOVED_PERMANENTLY)
        .headers(HTTP_HEADERS)
        .location(targetUrl)
        .build();
  }

}
