package com.springuni.hermes.core.orm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.springuni.hermes.click.ClickId;
import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TimeBasedIdentifierGeneratorTest {

    private static final String ENTITY_NAME = "test";
    private static final Object OBJECT = new Object();
    private static final long ID = 1;

    @Mock
    private SharedSessionContractImplementor session;

    @Mock
    private EntityPersister entityPersister;

    @Mock
    private Type type;

    private TimeBasedIdentifierGenerator timeBasedIdentifierGenerator;

    @Before
    public void setUp() throws Exception {
        timeBasedIdentifierGenerator = new TimeBasedIdentifierGenerator();
    }

    @Test
    public void configure() {
        givenNoIdAssigned();

        timeBasedIdentifierGenerator.generate(session, OBJECT);

        then(session).should().getEntityPersister(ENTITY_NAME, OBJECT);
    }

    @Test(expected = MappingException.class)
    public void configure_withWrongIdClass() {
        given(type.getReturnedClass()).willReturn(Object.class);

        timeBasedIdentifierGenerator.configure(type, getProperties(ENTITY_NAME), null);
    }

    @Test(expected = MappingException.class)
    public void configure_withoutEntityName() {
        configureWithEntityName(null);
    }

    @Test
    public void assigned() {
        givenIdAssigned(ID);

        Serializable id = timeBasedIdentifierGenerator.generate(session, OBJECT);

        assertEquals(ID, id);
    }

    @Test
    public void generated() {
        givenNoIdAssigned();

        Serializable id = timeBasedIdentifierGenerator.generate(session, OBJECT);

        assertNotNull(id);
        assertNotEquals(ID, id);
    }

    private void configureWithEntityName(String entityName) {
        Properties properties = getProperties(entityName);

        given(type.getReturnedClass()).willReturn(ClickId.class);

        timeBasedIdentifierGenerator.configure(type, properties, null);
    }

    private void givenIdAssigned(Long id) {
        configureWithEntityName(ENTITY_NAME);
        given(session.getEntityPersister(ENTITY_NAME, OBJECT)).willReturn(entityPersister);
        given(entityPersister.getIdentifier(OBJECT, session)).willReturn(id);
    }

    private void givenNoIdAssigned() {
        givenIdAssigned(null);
    }

    private Properties getProperties(String entityName) {
        Properties properties = new Properties();

        Optional.ofNullable(entityName)
                .ifPresent(it -> properties.put(IdentifierGenerator.ENTITY_NAME, entityName));

        return properties;
    }

}
