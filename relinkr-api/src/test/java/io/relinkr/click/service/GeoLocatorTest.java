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

import static io.relinkr.test.Mocks.REST_CLIENT_ERROR;
import static io.relinkr.test.Mocks.REST_IO_ERROR;
import static io.relinkr.test.Mocks.REST_SERVER_ERROR;
import static io.relinkr.test.Mocks.VISITOR_IP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;

import io.relinkr.click.service.GeoLocatorTest.TestConfig;
import io.relinkr.core.model.Country;
import io.relinkr.core.retry.RetryConfig;
import java.util.Optional;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestOperations;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class GeoLocatorTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  private RestOperations restOperations;

  @Autowired
  private GeoLocator geoLocator;

  @After
  public void tearDown() {
    reset(restOperations);
  }

  @Test
  public void givenNullIpAddress_whenLookupCountry_thenIllegalArgumentExceptionAndNotRetried() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("ipAddress is marked @NonNull but is null");

    try {
      geoLocator.lookupCountry(null);
    } finally {
      then(restOperations).should(never())
          .getForObject(anyString(), any(), anyMap());
    }
  }

  @Test
  public void givenIllegalCountyCodeReceived_whenLookupCountry_thenEmpty() {
    given(restOperations.getForObject(anyString(), any(), anyMap())).willReturn("bad");
    Optional<Country> country = geoLocator.lookupCountry(VISITOR_IP);
    assertFalse(country.isPresent());
    then(restOperations).should().getForObject(anyString(), any(), anyMap());
  }

  @Test
  public void givenUnknownCountyCodeReceived_whenLookupCountry_thenEmpty() {
    given(restOperations.getForObject(anyString(), any(), anyMap())).willReturn("SU");
    Optional<Country> country = geoLocator.lookupCountry(VISITOR_IP);
    assertEquals(Country.ZZ, country.get());
    then(restOperations).should().getForObject(anyString(), any(), anyMap());
  }

  @Test
  public void givenIOError_whenLookupCountry_thenRetriedAndExceptionPropagated() {
    assertRetried(REST_IO_ERROR, GeoLocatorImpl.MAX_ATTEMPTS);
  }

  @Test
  public void givenServerError_whenLookupCountry_thenRetriedAndExceptionPropagated() {
    assertRetried(REST_SERVER_ERROR, GeoLocatorImpl.MAX_ATTEMPTS);
  }

  @Test
  public void givenClientError_whenLookupCountry_thenRetriedAndExceptionPropagated() {
    assertRetried(REST_CLIENT_ERROR, 1);
  }

  private void assertRetried(Exception retryableException, int expectedAttempts) {
    given(restOperations.getForObject(anyString(), any(), anyMap()))
        .willThrow(retryableException);

    expectedException.expect(retryableException.getClass());
    expectedException.expectMessage(retryableException.getMessage());

    try {
      geoLocator.lookupCountry(VISITOR_IP);
    } finally {
      then(restOperations).should(times(expectedAttempts)).getForObject(
          anyString(), any(), anyMap()
      );
    }
  }

  @Configuration
  @Import(RetryConfig.class)
  static class TestConfig {

    @Bean
    RestOperations restOperations() {
      return mock(RestOperations.class);
    }

    @Bean
    GeoLocator geoLocator(RestOperations restOperations) {
      return new GeoLocatorImpl(restOperations);
    }

  }

}
