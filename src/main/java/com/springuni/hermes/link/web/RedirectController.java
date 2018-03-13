package com.springuni.hermes.link.web;

import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.springuni.hermes.core.ApplicationException;
import com.springuni.hermes.link.service.LinkService;
import java.net.URI;
import java.net.URL;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    private final LinkService linkService;

    public RedirectController(LinkService linkService) {
        this.linkService = linkService;
    }

    @RequestMapping(method = GET, value = "/{path}")
    public ResponseEntity redirectLink(@PathVariable(required = false) String path)
            throws ApplicationException {
        if (StringUtils.isEmpty(path)) {
            return notFound().build();
        }

        URL url = linkService.getTargetUrl(path);
        return ResponseEntity.status(MOVED_PERMANENTLY).location(URI.create(url.toString()))
                .build();
    }

}
