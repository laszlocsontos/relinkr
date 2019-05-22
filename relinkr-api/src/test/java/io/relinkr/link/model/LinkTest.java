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
  public void setUp() throws Exception {
    link = new Link(LONG_URL_WITHOUT_UTM, USER_ID);
  }

  @Test
  public void apply() {
    link.apply(UTM_PARAMETERS_MINIMAL);
    assertEquals(UTM_PARAMETERS_MINIMAL, link.longUrl.getUtmParameters().get());
  }

  @Test
  public void getTags() {
    assertTrue(link.getTags().isEmpty());
  }

  @Test
  public void addTag() {
    link.addTag(TAG_A);
    link.addTag(TAG_B);
    Set<Tag> tags = link.getTags();
    assertThat(tags, contains(TAG_A, TAG_B));
  }

  @Test
  public void removeTag() {
    link.addTag(TAG_A);
    link.addTag(TAG_B);
    link.removeTag(TAG_B);
    Set<Tag> tags = link.getTags();
    assertThat(tags, hasItem(TAG_A));
    assertThat(tags, not(hasItem(TAG_B)));
  }

  @Test
  public void getLongUrl() {
    assertEquals(LONG_URL_WITHOUT_UTM_S, link.getLongUrl().toString());
  }

  @Test
  public void getTargetUrl() {
    assertEquals(LONG_URL_WITHOUT_UTM_S, link.getTargetUrl().toString());
  }

  @Test
  public void getPath() {
    assertEquals(HASHIDS_LENGTH, link.getPath().length());
  }

  @Test(expected = InvalidUrlException.class)
  public void updateLongUrl_withInValidUrl() throws Exception {
    link.updateLongUrl("bad");
  }

  @Test
  public void updateLongUrl_withValidUrl() throws Exception {
    link.updateLongUrl(LONG_URL_BASE_S);
    assertEquals(LONG_URL_BASE_S, link.getLongUrl().toString());
    assertEquals(LONG_URL_BASE_S, link.getTargetUrl().toString());
  }

  @Test
  public void markActive() throws InvalidLinkStatusException {
    link.markActive();
    assertEquals(ACTIVE, link.getLinkStatus());
  }

  @Test
  public void markArchived() throws InvalidLinkStatusException {
    link.markActive();
    link.markArchived();
    assertEquals(ARCHIVED, link.getLinkStatus());
  }

  @Test
  public void markBroken() throws InvalidLinkStatusException {
    link.markBroken();
    assertEquals(BROKEN, link.getLinkStatus());
  }

  @Test(expected = InvalidLinkStatusException.class)
  public void markActive_withActive() throws InvalidLinkStatusException {
    link.markActive();
    link.markActive();
    assertEquals(ACTIVE, link.getLinkStatus());
  }

  @Test(expected = InvalidLinkStatusException.class)
  public void markArchived_withPending() throws InvalidLinkStatusException {
    link.markArchived();
    assertEquals(ARCHIVED, link.getLinkStatus());
  }

  @Test(expected = InvalidLinkStatusException.class)
  public void markBroken_withArchived() throws InvalidLinkStatusException {
    link.markArchived();
    link.markBroken();
    assertEquals(BROKEN, link.getLinkStatus());
  }

}
