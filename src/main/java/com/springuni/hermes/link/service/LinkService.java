package com.springuni.hermes.link.service;

import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.utm.UtmParameters;
import java.net.URL;

public interface LinkService extends LinkBaseService<Long, Link> {

    URL getTargetUrl(String path) throws ApplicationException;

    Link addLink(String baseUrl, UtmParameters utmParameters, Long userId)
            throws ApplicationException;

    Link updateLink(Long linkId, String baseUrl, UtmParameters utmParameters)
            throws ApplicationException;

}
