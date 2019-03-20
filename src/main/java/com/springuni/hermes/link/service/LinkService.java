package com.springuni.hermes.link.service;

import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.LinkId;
import com.springuni.hermes.link.model.UtmParameters;
import com.springuni.hermes.user.model.UserId;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LinkService {

    Link getLink(LinkId linkId);

    Link getLink(String path);

    Link addLink(String longUrl, UtmParameters utmParameters, UserId userId)
            throws ApplicationException;

    Page<Link> listLinks(UserId userId, Pageable pageable);

    void archiveLink(LinkId linkId);

    void activateLink(LinkId linkId);

    void addTag(LinkId linkId, String tagName);

    void removeTag(LinkId linkId, String tagName);

    Link updateLongUrl(LinkId linkId, String longUrl);

    Link updateLongUrl(LinkId linkId, String longUrl, UtmParameters utmParameters);

    URI getTargetUrl(String path) throws ApplicationException;

    Link updateUtmParameters(LinkId linkId, UtmParameters utmParameters);

}
