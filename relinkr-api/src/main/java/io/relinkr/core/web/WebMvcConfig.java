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

package io.relinkr.core.web;

import static java.lang.reflect.Modifier.FINAL;
import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;

import io.relinkr.click.model.ClickId;
import io.relinkr.core.convert.EntityClassAwareIdToStringConverter;
import io.relinkr.core.convert.StringToEntityClassAwareIdConverter;
import io.relinkr.link.model.LinkId;
import io.relinkr.core.model.UserId;
import io.relinkr.visitor.model.VisitorId;
import java.lang.reflect.Field;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private static final String APPA$BMP_CLASS =
      "org.springframework.hateoas.mvc.AnnotatedParametersParameterAccessor$BoundMethodParameter";

  private static final String APPA$BMP_CONVERSION_SERVICE = "CONVERSION_SERVICE";

  static final String FRONT_END_BASE_URL_PROPERTY = "relinkr.frontend.base-url";

  private final Environment environment;

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(String.class, ClickId.class,
        new StringToEntityClassAwareIdConverter<>(ClickId.class));
    registry.addConverter(new EntityClassAwareIdToStringConverter<ClickId>());

    registry.addConverter(String.class, LinkId.class,
        new StringToEntityClassAwareIdConverter<>(LinkId.class));
    registry.addConverter(new EntityClassAwareIdToStringConverter<LinkId>());

    registry.addConverter(String.class, VisitorId.class,
        new StringToEntityClassAwareIdConverter<>(VisitorId.class));
    registry.addConverter(new EntityClassAwareIdToStringConverter<VisitorId>());

    workaround((ConversionService) registry);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new CurrentUserArgumentResolver());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    String origin = environment.getRequiredProperty(FRONT_END_BASE_URL_PROPERTY);

    registry.addMapping("/**")
        .allowedOrigins(origin)
        .allowCredentials(true)
        .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE");
  }

  /*
   * Workaround for https://github.com/spring-projects/spring-hateoas/issues/118
   */
  private void workaround(ConversionService conversionService) {
    try {
      ReflectionUtils.doWithFields(
          Class.forName(APPA$BMP_CLASS),
          it -> setValue(it, conversionService),
          it -> APPA$BMP_CONVERSION_SERVICE.equals(it.getName())
      );
    } catch (ClassNotFoundException cnfe) {
      log.error(cnfe.getMessage(), cnfe);
    }
  }

  private void setValue(Field field, Object value) {
    // Remove final modifier
    Field modifiersField = findField(Field.class, "modifiers");
    makeAccessible(modifiersField);
    setField(modifiersField, field, field.getModifiers() & ~FINAL);

    // Set field value
    makeAccessible(field);
    setField(field, null, value);
  }

}
