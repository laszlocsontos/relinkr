package com.springuni.hermes.core.orm;

import com.springuni.hermes.core.util.IdentityGenerator;
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

public class TimeBasedIdentifierGenerator implements Configurable, IdentifierGenerator {

    private final ConcurrentMap<String, EntityPersister> CACHE = new ConcurrentHashMap<>();

    private final IdentityGenerator IDENTITY_GENERATOR = IdentityGenerator.getInstance();

    private String entityName;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry)
            throws MappingException {

        entityName = Optional
                .ofNullable(params.getProperty(ENTITY_NAME))
                .orElseThrow(() -> new MappingException("no entity name"));
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {

        EntityPersister entityPersister = CACHE
                .computeIfAbsent(entityName, it -> session.getEntityPersister(entityName, object));

        Serializable id = entityPersister.getIdentifier(object, session);

        return Optional.ofNullable(id).orElseGet(IDENTITY_GENERATOR::generate);
    }

}
