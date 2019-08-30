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

package io.relinkr.link.model;

import static io.relinkr.link.model.LinkStatus.PENDING;
import static java.util.Collections.unmodifiableSet;
import static javax.persistence.EnumType.STRING;

import io.relinkr.core.util.IdentityGenerator;
import io.relinkr.user.model.UserId;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import lombok.NonNull;
import org.hashids.Hashids;

/**
 * Represents a shortened link.
 */
@Entity
public class Link extends LinkBase<LinkId> {

  private static final long serialVersionUID = 7628232383862608990L;

  // FIXME: The theoretical maximum length could be decreased to 9 characters, according to
  //    round(log(Hashids.MAX_NUMBER) / log(len(HASHIDS_ALPHABET))).
  static final int HASHIDS_LENGTH = 10;

  private static final String HASHIDS_SALT = "6cY$S!08HpP$pWRpEErhGp7H3307a^67";

  private static final String HASHIDS_ALPHABET =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";

  private static final Hashids HASHIDS =
      new Hashids(HASHIDS_SALT, HASHIDS_LENGTH, HASHIDS_ALPHABET);

  @Embedded
  protected LongUrl longUrl;

  @Column(name = "path_")
  private String path;

  @Enumerated(STRING)
  private LinkStatus linkStatus = PENDING;

  @ElementCollection
  private Set<Tag> tags = new LinkedHashSet<>();

  public Link(
      @NonNull String longUrl, UtmParameters utmParameters, @NonNull UserId userId)
      throws InvalidUrlException {

    this(new LongUrl(longUrl, utmParameters), userId);
  }

  public Link(@NonNull String longUrl, @NonNull UserId userId) throws InvalidUrlException {
    this(longUrl, null, userId);
  }

  Link(@NonNull LongUrl longUrl, @NonNull UserId userId) {
    super(userId);
    this.longUrl = longUrl;
    path = generatePath();
  }

  /*
   * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
   */
  Link() {
  }

  public URI getLongUrl() {
    return longUrl.getLongUrl();
  }

  public String getPath() {
    return path;
  }

  public Optional<UtmParameters> getUtmParameters() {
    return longUrl.getUtmParameters();
  }

  public URI getTargetUrl() {
    return longUrl.getTargetUrl();
  }

  @Override
  public LinkStatus getLinkStatus() {
    return linkStatus;
  }

  @Override
  void setLinkStatus(LinkStatus linkStatus) {
    this.linkStatus = linkStatus;
  }

  /**
   * Applied the given {@code utmParameters} to this {@code Link}.
   *
   * @param utmParameters UTM parameters to apply
   */
  public void apply(UtmParameters utmParameters) {
    longUrl = longUrl.apply(utmParameters);
  }

  public Set<Tag> getTags() {
    return unmodifiableSet(tags);
  }

  public void addTag(Tag tag) {
    tags.add(tag);
  }

  public void removeTag(Tag tag) {
    tags.remove(tag);
  }

  @Override
  public String toString() {
    return getTargetUrl().toString();
  }

  public void updateLongUrl(@NonNull String longUrl) throws InvalidUrlException {
    updateLongUrl(longUrl, this.longUrl.getUtmParameters().orElse(null));
  }

  /**
   * Updates this {@code Link} with the given {@code longUrl} and {@code utmParameters}.
   *
   * @param longUrl A {@link LongUrl} used to update this {@code Link}
   * @param utmParameters An {@link UtmParameters} instance used to update this {@code Link}
   * @throws InvalidUrlException when the given {@code longUrl} is invalid
   */
  public void updateLongUrl(String longUrl, UtmParameters utmParameters)
      throws InvalidUrlException {
    this.longUrl = new LongUrl(longUrl, utmParameters);
  }

  private String generatePath() {
    long pathIdentity = IdentityGenerator.getInstance().generate();
    return HASHIDS.encode(pathIdentity);
  }

}
