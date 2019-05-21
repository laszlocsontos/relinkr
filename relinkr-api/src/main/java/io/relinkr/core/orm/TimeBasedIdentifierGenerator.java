package io.relinkr.core.orm;

import io.relinkr.core.convert.LongToEntityClassAwareIdConverter;
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

public class TimeBasedIdentifierGenerator implements Configurable, IdentifierGenerator {

  private final ConcurrentMap<String, EntityPersister> cache = new ConcurrentHashMap<>();

  private final IdentityGenerator identityGenerator = IdentityGenerator.getInstance();

  private Converter<Long, ? extends Serializable> conversionStrategy;
  private String entityName;

  @Override
  public void configure(Type type, Properties params, ServiceRegistry serviceRegistry)
      throws MappingException {

    Class<?> idClass = type.getReturnedClass();
    if (EntityClassAwareId.class.isAssignableFrom(idClass)) {
      conversionStrategy = new LongToEntityClassAwareIdConverter(idClass);
    } else if (Long.class.equals(idClass)) {
      conversionStrategy = (it -> it);
    } else if (long.class.equals(idClass)) {
      conversionStrategy = Long::valueOf;
    } else {
      throw new MappingException("unsupported class: " + idClass.getName());
    }

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

    return Optional
        .ofNullable(id)
        .orElseGet(() -> conversionStrategy.convert(identityGenerator.generate()));
  }

}
