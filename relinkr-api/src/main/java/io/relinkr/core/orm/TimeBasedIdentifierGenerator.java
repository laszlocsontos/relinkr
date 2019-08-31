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

package io.relinkr.core.orm;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.relinkr.core.convert.LongToEntityClassAwareIdConverter;
import io.relinkr.core.util.IdGenerator;
import io.relinkr.core.util.IdentityGenerator;
import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

/**
 * Identifier generation strategy based upon {@link IdentityGenerator}. It leverages
 * {@link LongToEntityClassAwareIdConverter} for converting simple long value to type-safe
 * identifier objects.
 */
@SuppressFBWarnings(
    value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR",
    justification = "Hibernate calls configure() before generate()"
)
public class TimeBasedIdentifierGenerator implements Configurable, IdentifierGenerator {

  private final ConcurrentMap<String, EntityPersister> cache = new ConcurrentHashMap<>();

  private final IdGenerator idGenerator;

  private Converter<Long, ? extends Serializable> conversionStrategy;
  private String entityName;

  public TimeBasedIdentifierGenerator() {
    idGenerator = new IdentityGenerator();
  }

  TimeBasedIdentifierGenerator(IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  @Override
  public void configure(Type type, Properties params, ServiceRegistry serviceRegistry)
      throws MappingException {

    conversionStrategy = initConversionStrategy(type.getReturnedClass());

    entityName = Optional
        .ofNullable(params.getProperty(ENTITY_NAME))
        .orElseThrow(() -> new MappingException("no entity name"));
  }

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object)
      throws HibernateException {

    EntityPersister entityPersister = cache
        .computeIfAbsent(entityName, it -> session.getEntityPersister(entityName, object));

    Serializable id = entityPersister.getIdentifier(object, session);

    Assert.notNull(
        conversionStrategy,
        "conversionStrategy has not been initialized in configure()"
    );

    return Optional
        .ofNullable(id)
        .orElseGet(() -> conversionStrategy.convert(idGenerator.generate()));
  }

  private Converter<Long, ? extends Serializable> initConversionStrategy(Class<?> idClass) {
    if (EntityClassAwareId.class.isAssignableFrom(idClass)) {
      return new LongToEntityClassAwareIdConverter(idClass);
    }

    if (Long.class.equals(idClass)) {
      return (it -> it);
    }

    if (long.class.equals(idClass)) {
      return Long::valueOf;
    }

    throw new MappingException("unsupported class: " + idClass.getName());
  }

}
