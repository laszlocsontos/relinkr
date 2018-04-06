package com.springuni.hermes.link.service;

import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.LinkId;
import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.utm.model.UtmParameters;
import java.net.URI;

public interface LinkService extends LinkBaseService<LinkId, Link> {

    URI getTargetUrl(String path) throws ApplicationException;

    Link addLink(String longUrl, UtmParameters utmParameters, UserId userId)
            throws ApplicationException;

    Link updateLongUrl(LinkId linkId, String longUrl, UtmParameters utmParameters);

    Link updateUtmParameters(LinkId linkId, UtmParameters utmParameters);

}
