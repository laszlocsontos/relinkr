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

/**
 * Custom {@link UserType} for being able to save {@link URI} values as
 * {@link java.sql.Types#VARCHAR} to the database and to get {@code URI} value back when data is
 * fetched.
 */
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
      throw new HibernateException("URI has invalid syntax - could not read entity", iae);
    }
  }

  @Override
  public Object nullSafeGet(
      ResultSet rs, String[] names,
      SharedSessionContractImplementor session, Object owner)
      throws HibernateException, SQLException {

    String value = (String) STRING.nullSafeGet(rs, names, session, owner);
    return fromStringValue(value);
  }

  @Override
  public void nullSafeSet(
      PreparedStatement st, Object value, int index,
      SharedSessionContractImplementor session) throws HibernateException, SQLException {

    String stringValue = toString((URI) value);
    STRING.nullSafeSet(st, stringValue, index, session);
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

}
