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
import static java.util.Collections.emptySet;
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

  private static final IdentityGenerator IDENTITY_GENERATOR = new IdentityGenerator();

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

  private Link(UserId userId, LongUrl longUrl, String path, LinkStatus linkStatus, Set<Tag> tags) {
    super(userId);

    this.longUrl = longUrl;
    this.path = path;
    this.linkStatus = linkStatus;
    this.tags = tags;
  }

  /*
   * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
   */
  Link() {
  }

  /**
   * Factory method for creation a new {@code Link}.
   *
   * @param userId  Owner's ID
   * @param longUrl Long URL
   * @return new Link
   */
  public static Link of(UserId userId, LongUrl longUrl) {
    String path = generatePath();
    return new Link(userId, longUrl, path, PENDING, emptySet());
  }

  /**
   * Factory method for creation a new {@code Link}.
   *
   * @param userId  Owner's ID
   * @param longUrl Long URL
   * @return new Link
   */
  public static Link of(UserId userId, String longUrl, UtmParameters utmParameters) {
    String path = generatePath();
    return new Link(userId, LongUrl.from(longUrl, utmParameters), path, PENDING, emptySet());
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
  Link setLinkStatus(LinkStatus linkStatus) {
    Link link = new Link(getUserId(), longUrl, path, linkStatus, tags);
    link.setId(this.getId());
    return link;
  }

  /**
   * Applied the given {@code utmParameters} to this {@code Link}.
   *
   * @param utmParameters UTM parameters to apply
   */
  public Link apply(UtmParameters utmParameters) {
    Link link = new Link(getUserId(), longUrl.apply(utmParameters), path, linkStatus, tags);
    link.setId(getId());
    return link;
  }

  public Set<Tag> getTags() {
    return unmodifiableSet(tags);
  }

  /**
   * @see LinkBase#addTag(Tag).
   *
   * @param tag Tag to add
   * @return new {@code Link}
   */
  public Link addTag(Tag tag) {
    Set<Tag> tags = new LinkedHashSet<>(this.tags);
    tags.add(tag);
    Link link = new Link(getUserId(), longUrl, path, linkStatus, tags);
    link.setId(getId());
    return link;
  }

  /**
   * @see LinkBase#removeTag(Tag).
   *
   * @param tag Tag to remove
   * @return new {@code Link}
   */
  public Link removeTag(Tag tag) {
    Set<Tag> tags = new LinkedHashSet<>(this.tags);
    tags.remove(tag);
    Link link = new Link(getUserId(), longUrl, path, linkStatus, tags);
    link.setId(getId());
    return link;
  }

  @Override
  public String toString() {
    return getTargetUrl().toString();
  }

  public Link updateLongUrl(@NonNull String longUrl) throws InvalidUrlException {
    return updateLongUrl(longUrl, this.longUrl.getUtmParameters().orElse(null));
  }

  /**
   * Updates this {@code Link} with the given {@code longUrl} and {@code utmParameters}.
   *
   * @param longUrl       A {@link LongUrl} used to update this {@code Link}
   * @param utmParameters An {@link UtmParameters} instance used to update this {@code Link}
   * @throws InvalidUrlException when the given {@code longUrl} is invalid
   */
  public Link updateLongUrl(String longUrl, UtmParameters utmParameters)
      throws InvalidUrlException {

    Link link = new Link(getUserId(), LongUrl.from(longUrl, utmParameters), path, linkStatus, tags);
    link.setId(this.getId());
    return link;
  }

  private static String generatePath() {
    long pathIdentity = IDENTITY_GENERATOR.generate();
    return HASHIDS.encode(pathIdentity);
  }

}
