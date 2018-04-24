package com.springuni.hermes.core.security.authn;

import static com.springuni.hermes.Mocks.ROOT_PATH;
import static com.springuni.hermes.Mocks.TEST_API_PATH;
import static com.springuni.hermes.Mocks.TEST_DASHBOARD_PATH;
import static com.springuni.hermes.Mocks.TEST_SHORT_LINK_PATH;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebRequestAuthorizationTestController {

    @GetMapping(ROOT_PATH)
    public HttpEntity getRoot() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(TEST_SHORT_LINK_PATH)
    public HttpEntity getShortLink() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(TEST_API_PATH)
    public HttpEntity getApi() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(TEST_DASHBOARD_PATH)
    public String dashboard() {
        return "pages/dashboard";
    }

}
