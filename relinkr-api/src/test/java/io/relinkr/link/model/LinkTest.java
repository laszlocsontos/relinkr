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

import static io.relinkr.link.model.Link.HASHIDS_LENGTH;
import static io.relinkr.link.model.LinkStatus.ACTIVE;
import static io.relinkr.link.model.LinkStatus.ARCHIVED;
import static io.relinkr.link.model.LinkStatus.BROKEN;
import static io.relinkr.test.Mocks.LONG_URL_BASE_S;
import static io.relinkr.test.Mocks.LONG_URL_WITHOUT_UTM;
import static io.relinkr.test.Mocks.LONG_URL_WITHOUT_UTM_S;
import static io.relinkr.test.Mocks.TAG_A;
import static io.relinkr.test.Mocks.TAG_B;
import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.UTM_PARAMETERS_MINIMAL;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class LinkTest {

  private Link link;

  @Before
  public void setUp() {
    link = Link.of(USER_ID, LONG_URL_WITHOUT_UTM);
  }

  @Test
  public void givenLinkWithoutUtm_whenApplyMinimalUtm_thenApplied() {
    Link link = this.link.apply(UTM_PARAMETERS_MINIMAL);
    assertEquals(UTM_PARAMETERS_MINIMAL, link.longUrl.getUtmParameters().get());
  }

  @Test
  public void givenLinkWithoutTags_whenGetTags_thenEmpty() {
    assertTrue(link.getTags().isEmpty());
  }

  @Test
  public void givenLinkWithoutTags_whenAddTag_thenAdded() {
    Set<Tag> tags = link
        .addTag(TAG_A)
        .addTag(TAG_B)
        .getTags();

    assertThat(tags, contains(TAG_A, TAG_B));
  }

  @Test
  public void givenLinkWithTags_whenRemoveTag_thenRemoved() {
    Set<Tag> tags = link
        .addTag(TAG_A)
        .addTag(TAG_B)
        .removeTag(TAG_B)
        .getTags();

    assertThat(tags, hasItem(TAG_A));
    assertThat(tags, not(hasItem(TAG_B)));
  }

  @Test
  public void givenLinkWithoutUtm_whenGetLongUrl_thenUtmIsMissing() {
    assertEquals(LONG_URL_WITHOUT_UTM_S, link.getLongUrl().toString());
  }

  @Test
  public void givenLinkWithoutUtm_whenGetTargetUrl_thenUtmIsMissing() {
    assertEquals(LONG_URL_WITHOUT_UTM_S, link.getTargetUrl().toString());
  }

  @Test
  public void givenNewLink_whenGetPath_thenPathHasExpectedLength() {
    assertEquals(HASHIDS_LENGTH, link.getPath().length());
  }

  @Test(expected = InvalidUrlException.class)
  public void givenInvalidUrl_whenUpdateLongUrl_thenInvalidUrlException() {
    link.updateLongUrl("bad");
  }

  @Test
  public void givenValidUrl_whenUpdateLongUrl_thenUpdated() {
    Link link = this.link.updateLongUrl(LONG_URL_BASE_S);
    assertEquals(LONG_URL_BASE_S, link.getLongUrl().toString());
    assertEquals(LONG_URL_BASE_S, link.getTargetUrl().toString());
  }

  @Test
  public void givenNewLink_whenMarkActive_thenActivated() {
    assertEquals(ACTIVE, link.markActive().getLinkStatus());
  }

  @Test
  public void givenActiveLink_whenMarkArchived_thenArchived() {
    assertEquals(ARCHIVED, link.markActive().markArchived().getLinkStatus());
  }

  @Test
  public void givenNewLink_whenMarkBroken_thenBroken() {
    assertEquals(BROKEN, link.markBroken().getLinkStatus());
  }

  @Test(expected = InvalidLinkStatusException.class)
  public void givenActiveLink_whenMarkActive_thenInvalidLinkStatusException() {
    assertEquals(ACTIVE, link.markActive().markActive().getLinkStatus());
  }

  @Test(expected = InvalidLinkStatusException.class)
  public void givenPendingLink_whenMarkArchived_thenInvalidLinkStatusException() {
    link.markArchived();
    assertEquals(ARCHIVED, link.markArchived().getLinkStatus());
  }

  @Test(expected = InvalidLinkStatusException.class)
  public void givenArchivedLink_whenMarkBroken_thenInvalidLinkStatusException() {
    assertEquals(BROKEN, link.markArchived().markBroken().getLinkStatus());
  }

}
