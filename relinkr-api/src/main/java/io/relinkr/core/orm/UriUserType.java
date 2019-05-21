package io.relinkr.core.orm;

import static org.hibernate.type.StandardBasicTypes.STRING;

import java.io.Serializable;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StringRepresentableType;
import org.hibernate.usertype.UserType;

public class UriUserType implements UserType, StringRepresentableType<URI> {

  private static final Class<URI> URI_CLASS = URI.class;
  private static final int[] URI_SQL_TYPES = new int[]{STRING.sqlType()};

  @Override
  public String toString(URI value) throws HibernateException {
    return Optional.ofNullable(value).map(URI::toString).orElse(null);
  }

  @Override
  public URI fromStringValue(String value) throws HibernateException {
    try {
      return Optional.ofNullable(value).map(URI::create).orElse(null);
    } catch (IllegalArgumentException iae) {
      throw translateException(iae);
    }
  }

  @Override
  public int[] sqlTypes() {
    return URI_SQL_TYPES;
  }

  @Override
  public Class returnedClass() {
    return URI_CLASS;
  }

  @Override
  public boolean equals(Object obj1, Object obj2) throws HibernateException {
    return Objects.equals(obj1, obj2);
  }

  @Override
  public int hashCode(Object obj) throws HibernateException {
    return Objects.hashCode(obj);
  }

  @Override
  public Object nullSafeGet(
      ResultSet rs, String[] names,
      SharedSessionContractImplementor session, Object owner)
      throws HibernateException, SQLException {

    String value = (String) STRING.nullSafeGet(rs, names, session, owner);

    try {
      return Optional.ofNullable(value).map(URI::create).orElse(null);
    } catch (IllegalArgumentException iae) {
      throw translateException(iae);
    }
  }

  @Override
  public void nullSafeSet(
      PreparedStatement st, Object value, int index,
      SharedSessionContractImplementor session) throws HibernateException, SQLException {

    String stringValue = Optional.ofNullable(value).map(Object::toString).orElse(null);
    STRING.nullSafeSet(st, stringValue, index, session);
  }

  @Override
  public Object deepCopy(Object value) throws HibernateException {
    // It is not necessary to copy immutable objects, or null values, in which case it is safe
    // to simply return the argument.
    return value;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(Object value) throws HibernateException {
    return (URI) value;
  }

  @Override
  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return cached;
  }

  @Override
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    // For immutable objects, or null values, it is safe to simply return the first parameter.
    return original;
  }

  private HibernateException translateException(IllegalArgumentException iae) {
    return new HibernateException("URI has invalid syntax - could not read entity", iae);
  }

}
