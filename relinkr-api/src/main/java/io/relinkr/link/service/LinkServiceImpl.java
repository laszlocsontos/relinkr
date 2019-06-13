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

import io.relinkr.core.model.EntityNotFoundException;
import io.relinkr.link.model.InvalidLinkStatusException;
import io.relinkr.link.model.InvalidUrlException;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import io.relinkr.link.model.LinkStatus;
import io.relinkr.link.model.Tag;
import io.relinkr.link.model.UtmParameters;
import io.relinkr.core.model.UserId;
import java.net.URI;
import lombok.NonNull;
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
  public Link getLink(@NonNull LinkId id) throws EntityNotFoundException {
    return linkRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("id", id));

  }

  @Override
  public Link getLink(String path) throws EntityNotFoundException {
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
  public Page<Link> fetchLinks(@NonNull UserId userId, Pageable pageable) {
    return linkRepository.findByUserId(userId, pageable);
  }

  @Override
  public Link addLink(String longUrl, UtmParameters utmParameters, @NonNull UserId userId)
      throws InvalidUrlException {

    Assert.hasText(longUrl, "longUrl must contain text");
    Assert.notNull(userId, "userId cannot be null");

    Link link = new Link(longUrl, utmParameters, userId);
    link.markActive();

    return linkRepository.save(link);
  }

  @Override
  public void activateLink(@NonNull LinkId id)
      throws EntityNotFoundException, InvalidLinkStatusException {
    Link link = getLink(id);
    link.markActive();

    linkRepository.save(link);
  }

  @Override
  public void archiveLink(@NonNull LinkId id)
      throws EntityNotFoundException, InvalidLinkStatusException {
    Link link = getLink(id);
    link.markArchived();

    linkRepository.save(link);
  }

  @Override
  public void addTag(@NonNull LinkId id, String tagName) throws EntityNotFoundException {
    Assert.hasText(tagName, "tagName must contain text");

    Link link = getLink(id);
    link.addTag(new Tag(tagName));

    linkRepository.save(link);
  }

  @Override
  public void removeTag(@NonNull LinkId id, String tagName) throws EntityNotFoundException {
    Assert.hasText(tagName, "tagName must contain text");

    Link link = getLink(id);
    link.removeTag(new Tag(tagName));
    linkRepository.save(link);
  }

  @Override
  public Link updateLongUrl(@NonNull LinkId id, String longUrl) throws EntityNotFoundException {
    Assert.hasText(longUrl, "longUrl must contain text");

    Link link = getLink(id);
    link.updateLongUrl(longUrl);
    return linkRepository.save(link);
  }

  @Override
  public Link updateLongUrl(@NonNull LinkId linkId, String longUrl, UtmParameters utmParameters)
      throws EntityNotFoundException {

    Assert.hasText(longUrl, "longUrl must contain text");

    Link link = getLink(linkId);
    link.updateLongUrl(longUrl, utmParameters);
    return linkRepository.save(link);
  }

  @Override
  public URI getTargetUrl(String path) throws EntityNotFoundException {
    return getLink(path).getTargetUrl();
  }

  @Override
  public Link updateUtmParameters(@NonNull LinkId linkId, UtmParameters utmParameters)
      throws EntityNotFoundException {

    Link link = getLink(linkId);
    link.apply(utmParameters);
    return linkRepository.save(link);
  }

}
