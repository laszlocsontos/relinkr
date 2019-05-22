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
