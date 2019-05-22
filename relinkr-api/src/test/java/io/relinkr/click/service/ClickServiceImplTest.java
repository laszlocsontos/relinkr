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
 
package io.relinkr.click.service;

import static io.relinkr.test.Mocks.REST_SERVER_ERROR;
import static io.relinkr.test.Mocks.VISITOR_IP;
import static io.relinkr.test.Mocks.createClick;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import io.relinkr.click.model.Click;
import io.relinkr.core.model.Country;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClickServiceImplTest {

  @Mock
  private ClickRepository clickRepository;

  @Captor
  private ArgumentCaptor<Click> clickArgumentCaptor;

  @Mock
  private GeoLocator geoLocator;

  private Click click;

  private ClickService clickService;

  @Before
  public void setUp() {
    click = createClick();
    clickService = new ClickServiceImpl(clickRepository, geoLocator);
  }

  @Test
  public void givenGeoLookupFailed_whenLogClick_savedWithoutCountry() {
    given(geoLocator.lookupCountry(VISITOR_IP)).willThrow(REST_SERVER_ERROR);
    clickService.logClick(click);
    then(clickRepository).should().save(clickArgumentCaptor.capture());
    assertClick(click, Optional.empty());
  }

  @Test
  public void givenGeoLookupNotFoundCountry_whenLogClick_savedWithoutCountry() {
    given(geoLocator.lookupCountry(VISITOR_IP)).willReturn(Optional.empty());
    clickService.logClick(click);
    then(clickRepository).should().save(clickArgumentCaptor.capture());
    assertClick(click, Optional.empty());
  }

  @Test
  public void givenGeoLookupFoundCountry_whenLogClick_savedWithCountry() {
    given(geoLocator.lookupCountry(VISITOR_IP)).willReturn(Optional.of(Country.US));
    clickService.logClick(click);
    then(clickRepository).should().save(clickArgumentCaptor.capture());
    assertClick(click, Optional.of(Country.US));
  }

  private void assertClick(Click expectedClick, Optional<Country> expectedCountry) {
    Click click = clickArgumentCaptor.getValue();
    assertEquals(expectedClick.getLinkId(), click.getLinkId());
    assertEquals(expectedClick.getVisitorId(), click.getVisitorId());
    assertEquals(expectedClick.getUserId(), click.getUserId());
    assertEquals(expectedClick.getVisitorIp(), click.getVisitorIp());
    assertEquals(expectedClick.getVisitTimestamp(), click.getVisitTimestamp());
    assertEquals(expectedCountry, click.getCountry());
  }

}
