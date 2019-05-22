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

import static io.relinkr.link.model.UtmParameters.UTM_CAMPAIGN;
import static io.relinkr.link.model.UtmParameters.UTM_CONTENT;
import static io.relinkr.link.model.UtmParameters.UTM_MEDIUM;
import static io.relinkr.link.model.UtmParameters.UTM_SOURCE;
import static io.relinkr.link.model.UtmParameters.UTM_TERM;
import static io.relinkr.test.Mocks.UTM_CAMPAIGN_V;
import static io.relinkr.test.Mocks.UTM_CONTENT_V;
import static io.relinkr.test.Mocks.UTM_MEDIUM_V;
import static io.relinkr.test.Mocks.UTM_PARAMETERS_FULL;
import static io.relinkr.test.Mocks.UTM_SOURCE_V;
import static io.relinkr.test.Mocks.UTM_TERM_V;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Map;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;

public class UtmParametersTest {

  @Test
  public void asMap() {
    Map<String, String> utmParameterMap = UTM_PARAMETERS_FULL.asMap();
    assertThat(utmParameterMap, IsMapContaining.hasEntry(UTM_SOURCE, UTM_SOURCE_V));
    assertThat(utmParameterMap, IsMapContaining.hasEntry(UTM_MEDIUM, UTM_MEDIUM_V));
    assertThat(utmParameterMap, IsMapContaining.hasEntry(UTM_CAMPAIGN, UTM_CAMPAIGN_V));
    assertThat(utmParameterMap, IsMapContaining.hasEntry(UTM_TERM, UTM_TERM_V));
    assertThat(utmParameterMap, IsMapContaining.hasEntry(UTM_CONTENT, UTM_CONTENT_V));
  }

  @Test(expected = MissingUtmParameterException.class)
  public void create_withMissingUtmSource() throws Exception {
    new UtmParameters(null, "test", "test");
  }

  @Test(expected = MissingUtmParameterException.class)
  public void create_withMissingUtmMedium() throws Exception {
    new UtmParameters("test", null, "test");
  }

  @Test(expected = MissingUtmParameterException.class)
  public void create_withMissingUtmCampaign() throws Exception {
    new UtmParameters("test", "test", null);
  }

  @Test
  public void getUtmSource() {
    assertEquals(UTM_SOURCE_V, UTM_PARAMETERS_FULL.getUtmSource());
  }

  @Test
  public void getUtmMedium() {
    assertEquals(UTM_MEDIUM_V, UTM_PARAMETERS_FULL.getUtmMedium());
  }

  @Test
  public void getUtmCampaign() {
    assertEquals(UTM_CAMPAIGN_V, UTM_PARAMETERS_FULL.getUtmCampaign());
  }

  @Test
  public void getUtmTerm() {
    assertEquals(UTM_TERM_V, UTM_PARAMETERS_FULL.getUtmTerm().get());
  }

  @Test
  public void getUtmContent() {
    assertEquals(UTM_CONTENT_V, UTM_PARAMETERS_FULL.getUtmContent().get());
  }

}
