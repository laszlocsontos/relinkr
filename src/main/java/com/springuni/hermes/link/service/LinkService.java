package com.springuni.hermes.link.service;

import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.utm.model.UtmParameters;
import java.net.URI;

public interface LinkService extends LinkBaseService<Long, Link> {

    URI getTargetUrl(String path) throws ApplicationException;

    Link addLink(String longUrl, UtmParameters utmParameters, Long userId)
            throws ApplicationException;

    Link updateLongUrl(Long linkId, String longUrl, UtmParameters utmParameters);

    Link updateUtmParameters(Long linkId, UtmParameters utmParameters);

}
