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

import static org.springframework.util.Assert.isTrue;

import io.relinkr.core.web.AbstractCookieValueResolver;
import io.relinkr.core.web.CookieManager;
import io.relinkr.core.web.JwsCookieManager;
import io.relinkr.visitor.model.VisitorId;
import java.time.Duration;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class VisitorIdCookieResolverImpl
    extends AbstractCookieValueResolver<VisitorId> implements VisitorIdCookieResolver {

  static final String COOKIE_NAME = "vid";

  // Three years
  static final Duration COOKIE_MAX_AGE = Duration.ofDays(3 * 365);

  static final String VISITOR_SECRET_KEY_PROPERTY = "relinkr.cookie.visitor-secret-key";

  private final CookieManager cookieManager;
  private final ConversionService conversionService;

  @Autowired
  public VisitorIdCookieResolverImpl(
      ConversionService conversionService, Environment environment) {

    String secretKey =
        environment.getRequiredProperty(VISITOR_SECRET_KEY_PROPERTY);

    cookieManager = new JwsCookieManager(COOKIE_NAME, COOKIE_MAX_AGE, secretKey);

    assertCanConvert(conversionService, String.class, VisitorId.class);
    assertCanConvert(conversionService, VisitorId.class, String.class);
    this.conversionService = conversionService;
  }

  private void assertCanConvert(
      ConversionService conversionService, Class<?> sourceType, Class<?> targetType) {

    isTrue(
        conversionService.canConvert(sourceType, targetType),
        "conversionService is not configured for converting ["
            + sourceType.getSimpleName() + "] to [" + targetType.getSimpleName() + "]"
    );
  }

  @Override
  protected CookieManager getCookieManager() {
    return cookieManager;
  }

  @Override
  protected Optional<VisitorId> fromString(String value) {
    return Optional.ofNullable(convert(value, VisitorId.class));
  }

  @Override
  protected Optional<String> toString(VisitorId value) {
    return Optional.ofNullable(convert(value, String.class));
  }

  private <S, T> T convert(S source, Class<T> targetClass) {
    if (source == null) {
      return null;
    }

    try {
      return conversionService.convert(source, targetClass);
    } catch (ConversionFailedException cfe) {
      // Conversion failed for whatever reason
      return null;
    }
  }

}
