/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
 
package io.relinkr.link.service;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.model.EntityNotFoundException;
import io.relinkr.link.model.InvalidUrlException;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import io.relinkr.link.model.LinkStatus;
import io.relinkr.link.model.Tag;
import io.relinkr.link.model.UtmParameters;
import io.relinkr.user.model.UserId;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
class LinkServiceImpl implements LinkService {

  private final LinkRepository linkRepository;

  @Override
  public Link getLink(LinkId id) throws EntityNotFoundException {
    Assert.notNull(id, "id cannot be null");

    return linkRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("id", id));

  }

  @Override
  public Link getLink(String path) {
    Assert.hasText(path, "path must contain text");

    Link link = linkRepository
        .findByPath(path)
        .orElseThrow(() -> new EntityNotFoundException("path", path));

    if (!LinkStatus.ACTIVE.equals(link.getLinkStatus())) {
      throw new EntityNotFoundException("path", path);
    }

    return link;
  }

  @Override
  public Page<Link> listLinks(UserId userId, Pageable pageable) {
    return linkRepository.findByUserId(userId, pageable);
  }

  @Override
  public void activateLink(LinkId id) throws ApplicationException {
    Assert.notNull(id, "id cannot be null");

    Link link = getLink(id);
    verifyLinkBeforeUpdate(link);
    link.markActive();

    linkRepository.save(link);
  }

  @Override
  public void archiveLink(LinkId id) throws ApplicationException {
    Assert.notNull(id, "id cannot be null");

    Link link = getLink(id);
    verifyLinkBeforeUpdate(link);
    link.markArchived();

    linkRepository.save(link);
  }

  @Override
  public void addTag(LinkId id, String tagName) throws ApplicationException {
    Assert.notNull(id, "id cannot be null");
    Assert.hasText(tagName, "tagName must contain text");

    Link link = getLink(id);
    verifyLinkBeforeUpdate(link);
    link.addTag(new Tag(tagName));

    linkRepository.save(link);
  }

  @Override
  public void removeTag(LinkId id, String tagName) throws ApplicationException {
    Assert.notNull(id, "id cannot be null");
    Assert.hasText(tagName, "tagName must contain text");

    Link link = getLink(id);
    verifyLinkBeforeUpdate(link);
    link.removeTag(new Tag(tagName));
    linkRepository.save(link);
  }

  @Override
  public Link updateLongUrl(LinkId id, String longUrl) {
    Assert.notNull(id, "id cannot be null");
    Assert.hasText(longUrl, "longUrl must contain text");

    Link link = getLink(id);
    link.updateLongUrl(longUrl);
    return linkRepository.save(link);
  }

  @Override
  public Link updateLongUrl(LinkId linkId, String longUrl, UtmParameters utmParameters) {
    Assert.notNull(linkId, "linkId cannot be null");
    Assert.hasText(longUrl, "longUrl must contain text");

    Link link = getLink(linkId);
    verifyLinkBeforeUpdate(link);
    link.updateLongUrl(longUrl, utmParameters);
    return linkRepository.save(link);
  }

  @Override
  public URI getTargetUrl(String path) throws EntityNotFoundException {
    return getLink(path).getTargetUrl();
  }

  @Override
  public Link addLink(String longUrl, UtmParameters utmParameters, UserId userId)
      throws InvalidUrlException {

    Assert.hasText(longUrl, "longUrl must contain text");
    Assert.notNull(userId, "userId cannot be null");

    Link link = new Link(longUrl, utmParameters, userId);
    return linkRepository.save(link);
  }

  @Override
  public Link updateUtmParameters(LinkId linkId, UtmParameters utmParameters)
      throws ApplicationException {

    Assert.notNull(linkId, "linkId cannot be null");

    Link link = getLink(linkId);
    verifyLinkBeforeUpdate(link);
    link.apply(utmParameters);
    return linkRepository.save(link);
  }

  protected void verifyLinkBeforeUpdate(Link link) {
    // TODO: I don't remember what this was supposed to be doing
  }

}
