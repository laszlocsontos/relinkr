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

import static io.relinkr.test.Mocks.LONG_URL_BASE;
import static io.relinkr.test.Mocks.LONG_URL_BASE_S;
import static io.relinkr.test.Mocks.LONG_URL_INVALID_UTM;
import static io.relinkr.test.Mocks.LONG_URL_VALID_UTM;
import static io.relinkr.test.Mocks.LONG_URL_VALID_UTM_EMPTY_TC;
import static io.relinkr.test.Mocks.LONG_URL_VALID_UTM_S;
import static io.relinkr.test.Mocks.LONG_URL_WITHOUT_UTM;
import static io.relinkr.test.Mocks.LONG_URL_WITHOUT_UTM_S;
import static io.relinkr.test.Mocks.UTM_PARAMETERS_FULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import org.junit.Test;
import org.springframework.web.util.UriComponents;

public class LongUrlTest {

  @Test
  public void givenLongUrlBase_whenCreate_thenTargetUrlIsLongUrlBase() {
    assertEquals(URI.create(LONG_URL_BASE_S), LONG_URL_BASE.getTargetUrl());
  }

  @Test
  public void givenLongUrlWithoutUtm_whenCreate_thenTargetUrlIsWithoutUtm() {
    assertEquals(URI.create(LONG_URL_WITHOUT_UTM_S), LONG_URL_WITHOUT_UTM.getTargetUrl());
  }

  @Test
  public void givenLongUrlWithInvalidUtm_whenCreate_thenTargetUrlIsWithoutUtm() {
    assertEquals(URI.create(LONG_URL_WITHOUT_UTM_S), LONG_URL_INVALID_UTM.getTargetUrl());
  }

  @Test
  public void givenLongUrlWithValidUtm_whenCreate_thenTargetUrlIsWithUtm() {
    assertEquals(URI.create(LONG_URL_VALID_UTM_S), LONG_URL_VALID_UTM.getTargetUrl());
  }

  @Test
  public void givenLongUrlWithUtmButWithoutTerm_whenCreate_thenTargetUrlIsWithoutTerm() {
    assertEquals(URI.create(LONG_URL_VALID_UTM_S), LONG_URL_VALID_UTM_EMPTY_TC.getTargetUrl());
  }

  @Test
  public void givenLongUrlBase_whenHasUtmParameters_thenFalse() {
    assertFalse(LONG_URL_BASE.hasUtmParameters());
  }

  @Test
  public void givenLongUrlWithoutUtm_whenHasUtmParameters_thenFalse() {
    assertFalse(LONG_URL_WITHOUT_UTM.hasUtmParameters());
  }

  @Test
  public void givenLongUrlInvalidUtm_whenHasUtmParameters_thenFalse() {
    assertFalse(LONG_URL_INVALID_UTM.hasUtmParameters());
  }

  @Test
  public void givenLongUrlValidUtm_whenHasUtmParameters_thenTrue() {
    assertTrue(LONG_URL_VALID_UTM.hasUtmParameters());
  }

  @Test
  public void givenLongUrlBaseAndFullUtm_whenApply_thenApplied() {
    assertEquals(UTM_PARAMETERS_FULL,
        LONG_URL_BASE.apply(UTM_PARAMETERS_FULL).getUtmParameters().get());
  }

  @Test
  public void givenLongUrlBaseWithoutUtm_whenApply_thenApplied() {
    assertEquals(UTM_PARAMETERS_FULL,
        LONG_URL_WITHOUT_UTM.apply(UTM_PARAMETERS_FULL).getUtmParameters().get());
  }

  @Test
  public void givenLongUrlWithInvalidUtmAndFullUtm_whenApply_thenApplied() {
    assertEquals(UTM_PARAMETERS_FULL,
        LONG_URL_INVALID_UTM.apply(UTM_PARAMETERS_FULL).getUtmParameters().get());
  }

  @Test
  public void givenLongUrlWithValidUtmAndFullUtm_whenApply_thenApplied() {
    assertEquals(UTM_PARAMETERS_FULL,
        LONG_URL_VALID_UTM.apply(UTM_PARAMETERS_FULL).getUtmParameters().get());
  }

  @Test(expected = InvalidUrlException.class)
  public void givenLocalIpAddress_whenParseUrl_thenInvalidUrlException() {
    LongUrl.parseUrl("http://10.20.53.1");
  }

  @Test
  public void givenPublicIpAddress_whenParseUrl() {
    UriComponents uriComponents = LongUrl.parseUrl("http://216.58.214.238");
    assertEquals(uriComponents.getScheme(), "http");
    assertEquals(uriComponents.getHost(), "216.58.214.238");
  }

  @Test(expected = InvalidUrlException.class)
  public void givenFtpUrl_whenParseUrl_thenInvalidUrlException() {
    LongUrl.parseUrl("ftp://google.com");
  }

  @Test
  public void givenHttpUrl_whenParseUrl_thenParsed() {
    UriComponents uriComponents = LongUrl.parseUrl("http://google.com");
    assertEquals(uriComponents.getScheme(), "http");
    assertEquals(uriComponents.getHost(), "google.com");
  }

  @Test
  public void givenHttpsUrl_whenParseUrl_thenParsed() {
    UriComponents uriComponents = LongUrl.parseUrl("https://google.com");
    assertEquals(uriComponents.getScheme(), "https");
    assertEquals(uriComponents.getHost(), "google.com");
  }

  @Test
  public void givenHttpsUrlWithFragment_whenParseUrl_thenParsed() {
    UriComponents uriComponents = LongUrl.parseUrl("https://google.com#test");
    assertEquals(uriComponents.getScheme(), "https");
    assertEquals(uriComponents.getHost(), "google.com");
    assertEquals(uriComponents.getFragment(), "test");
  }

}
