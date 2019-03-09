package com.springuni.hermes.core.web;

import static java.lang.reflect.Modifier.FINAL;
import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;

import com.springuni.hermes.click.ClickId;
import com.springuni.hermes.core.convert.EntityClassAwareIdToStringConverter;
import com.springuni.hermes.core.convert.StringToEntityClassAwareIdConverter;
import com.springuni.hermes.link.model.LinkId;
import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.visitor.model.VisitorId;
import java.lang.reflect.Field;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String APPA$BMP_CLASS =
            "org.springframework.hateoas.mvc.AnnotatedParametersParameterAccessor$BoundMethodParameter";

    private static final String APPA$BMP_CONVERSION_SERVICE = "CONVERSION_SERVICE";

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, ClickId.class,
                new StringToEntityClassAwareIdConverter<>(ClickId.class));
        registry.addConverter(new EntityClassAwareIdToStringConverter<ClickId>());

        registry.addConverter(String.class, LinkId.class,
                new StringToEntityClassAwareIdConverter<>(LinkId.class));
        registry.addConverter(new EntityClassAwareIdToStringConverter<LinkId>());

        registry.addConverter(String.class, UserId.class,
                new StringToEntityClassAwareIdConverter<>(UserId.class));
        registry.addConverter(new EntityClassAwareIdToStringConverter<UserId>());

        registry.addConverter(String.class, VisitorId.class,
                new StringToEntityClassAwareIdConverter<>(VisitorId.class));
        registry.addConverter(new EntityClassAwareIdToStringConverter<VisitorId>());

        workaround((ConversionService) registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver());
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
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
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
