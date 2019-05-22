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
 
package io.relinkr.visitor.web;

import static io.relinkr.test.Mocks.JWS_VISITOR_COOKIE_SECRET_KEY;
import static io.relinkr.test.Mocks.JWS_VISITOR_COOKIE_VALUE;
import static io.relinkr.test.Mocks.VISITOR_ID;

import io.relinkr.core.convert.EntityClassAwareIdToStringConverter;
import io.relinkr.core.convert.StringToEntityClassAwareIdConverter;
import io.relinkr.core.web.AbstractCookieValueResolver;
import io.relinkr.core.web.AbstractCookieValueResolverTest;
import io.relinkr.visitor.model.VisitorId;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

public class VisitorIdCookieResolverTest extends AbstractCookieValueResolverTest<VisitorId> {

  private final ConfigurableConversionService conversionService;

  public VisitorIdCookieResolverTest() {
    super(VisitorIdCookieResolverImpl.COOKIE_NAME, VisitorIdCookieResolverImpl.COOKIE_MAX_AGE,
        VISITOR_ID, JWS_VISITOR_COOKIE_VALUE);

    conversionService = new GenericConversionService();

    conversionService.addConverter(
        String.class,
        VisitorId.class,
        new StringToEntityClassAwareIdConverter<>(VisitorId.class)
    );

    conversionService.addConverter(new EntityClassAwareIdToStringConverter<VisitorId>());
  }

  @Override
  protected void setUpEnvironment(MockEnvironment environment) {
    environment.setProperty(VisitorIdCookieResolverImpl.VISITOR_SECRET_KEY_PROPERTY,
        JWS_VISITOR_COOKIE_SECRET_KEY);
  }

  @Override
  protected AbstractCookieValueResolver<VisitorId> createCookieValueResolver(
      Environment environment) {

    return new VisitorIdCookieResolverImpl(conversionService, environment);
  }

}
