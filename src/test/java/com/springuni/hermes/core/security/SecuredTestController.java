package com.springuni.hermes.core.security;

import static com.springuni.hermes.Mocks.ROOT_PATH;
import static com.springuni.hermes.Mocks.TEST_API_PATH;
import static com.springuni.hermes.Mocks.TEST_SHORT_LINK_PATH;

import com.springuni.hermes.Mocks;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredTestController {


    @GetMapping(ROOT_PATH)
    public HttpEntity root() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(TEST_SHORT_LINK_PATH)
    public HttpEntity shortLink() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(TEST_API_PATH)
    public HttpEntity getLink() {
        return ResponseEntity.ok().build();
    }

}
