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
import io.relinkr.link.model.UtmParameters;
import io.relinkr.user.model.UserId;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Provides the service layer for {@link Link}s, that is, takes care of creating, updating, changing
 * the status of links.
 */
public interface LinkService {

  /**
   * Returns a link by its ID.
   *
   * @param linkId link's ID
   * @return the link if exists
   * @throws EntityNotFoundException is thrown if the link doesn't exist
   */
  Link getLink(LinkId linkId) throws EntityNotFoundException;

  /**
   * Returns a link by its path.
   *
   * @param path link's path
   * @return the link if exists
   * @throws EntityNotFoundException is thrown if the link doesn't exist
   */
  Link getLink(String path) throws EntityNotFoundException;

  /**
   * Fetches a paged slice of links by the given {@code userId}.
   *
   * @param userId user's ID owning the links to be fetched
   * @param pageable {@link Pageable} defining the parameters of paging
   * @return paged links
   */
  Page<Link> fetchLinks(UserId userId, Pageable pageable);

  /**
   * Add a new link.
   *
   * @param longUrl long url to shorten
   * @param utmParameters UTM parameters (can be {@code null}
   * @param userId user's ID (cannot be {@code null}
   * @return A newly added link
   * @throws InvalidUrlException is thrown if {@code longUrl} isn't a valid URL
   */
  Link addLink(String longUrl, UtmParameters utmParameters, UserId userId)
      throws InvalidUrlException;

  /**
   * Activates the given link.
   *
   * @param linkId link's ID to be activated
   * @throws EntityNotFoundException is thrown if the link doesn't exist
   * @throws InvalidLinkStatusException is thrown if the link next status cannot be {@code ACTIVE}
   */
  void activateLink(LinkId linkId) throws EntityNotFoundException, InvalidLinkStatusException;

  /**
   * Archived the given link.
   *
   * @param linkId link's ID to be activated
   * @throws EntityNotFoundException is thrown if the link doesn't exist
   * @throws InvalidLinkStatusException is thrown if the link next status cannot be {@code ARCHIVED}
   */
  void archiveLink(LinkId linkId) throws EntityNotFoundException, InvalidLinkStatusException;

  /**
   * Add the given tag to a link.
   *
   * @param linkId link's ID to be tagged
   * @param tagName tag to be added
   * @throws EntityNotFoundException is thrown if the link doesn't exist
   */
  void addTag(LinkId linkId, String tagName) throws EntityNotFoundException;

  /**
   * Removed the given tag from a link.
   *
   * @param linkId link's ID to be de-tagged
   * @param tagName tag to be removed
   * @throws EntityNotFoundException is thrown if the link doesn't exist
   */
  void removeTag(LinkId linkId, String tagName) throws EntityNotFoundException;

  /**
   * Updates a link's {@link io.relinkr.link.model.LongUrl}.
   *
   * @param linkId link's ID to be updated
   * @param longUrl long URL as string
   * @return the updated link
   * @throws EntityNotFoundException is thrown if the link doesn't exist
   */
  Link updateLongUrl(LinkId linkId, String longUrl) throws EntityNotFoundException;

  /**
   * Updates a link's {@link io.relinkr.link.model.LongUrl} and {@link UtmParameters}.
   *
   * @param linkId link's ID to be updated
   * @param longUrl long URL as string
   * @param utmParameters UTM parameters
   * @return the updated link
   * @throws EntityNotFoundException is thrown if the link doesn't exist
   */
  Link updateLongUrl(LinkId linkId, String longUrl, UtmParameters utmParameters)
      throws EntityNotFoundException;

  /**
   * Get's the link target URL.
   *
   * @param path identifying the link
   * @return target URL
   * @throws EntityNotFoundException is thrown if the link doesn't exist
   */
  URI getTargetUrl(String path) throws EntityNotFoundException;

  /**
   * Updates a link's {@link UtmParameters}.
   *
   * @param linkId link's ID to be updated
   * @param utmParameters UTM parameters
   * @return the updated link
   * @throws EntityNotFoundException is thrown if the link doesn't exist
   */
  Link updateUtmParameters(LinkId linkId, UtmParameters utmParameters)
      throws EntityNotFoundException;

}
