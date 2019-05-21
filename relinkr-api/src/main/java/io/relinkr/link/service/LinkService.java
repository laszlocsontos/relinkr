package io.relinkr.link.service;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import io.relinkr.link.model.UtmParameters;
import io.relinkr.user.model.UserId;
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
