package com.springuni.hermes.link;

import com.springuni.hermes.core.ApplicationException;
import com.springuni.hermes.utm.UtmParameters;
import java.net.URL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LinkService {

    URL getTargetUrl(String path) throws ApplicationException;

    Link getLink(long linkId) throws ApplicationException;

    Link addLink(String baseUrl, UtmParameters utmParameters, Long userId)
            throws ApplicationException;

    Link updateLink(long linkId, String baseUrl, UtmParameters utmParameters)
            throws ApplicationException;

    Page<Link> listLinks(long userId, Pageable pageable);

    void activateLink(long linkId) throws ApplicationException;

    void archiveLink(long linkId) throws ApplicationException;

    void addTag(long linkId, String tagName) throws ApplicationException;

    void removeTag(long linkId, String tagName) throws ApplicationException;

}
