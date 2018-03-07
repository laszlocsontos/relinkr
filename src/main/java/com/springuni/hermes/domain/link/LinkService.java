package com.springuni.hermes.domain.link;

import com.springuni.hermes.core.ApplicationException;
import com.springuni.hermes.core.EntityNotFoundException;
import com.springuni.hermes.domain.utm.UtmParameters;
import java.net.URL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LinkService {

    URL getTargetUrl(String path) throws EntityNotFoundException;

    Link getLink(long linkId) throws EntityNotFoundException;

    Link addLink(String baseUrl, UtmParameters utmParameters) throws InvalidUrlException;

    Link updateLink(long linkId, String baseUrl, UtmParameters utmParameters) throws ApplicationException;

    Page<StandaloneLink> listLinks(long userId, Pageable pageable);

    void archiveLink(long linkId) throws EntityNotFoundException;

    void addTag(long linkId, String tagName) throws ApplicationException;

    void removeTag(long linkId, String tagName) throws EntityNotFoundException;

}
