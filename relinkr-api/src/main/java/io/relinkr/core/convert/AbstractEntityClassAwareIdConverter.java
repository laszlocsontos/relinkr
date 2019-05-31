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

package io.relinkr.core.convert;

import static org.springframework.beans.BeanUtils.instantiateClass;
import static org.springframework.util.ClassUtils.getConstructorIfAvailable;

import io.relinkr.core.orm.EntityClassAwareId;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Optional;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

/**
 * Abstract base class for converting a simple value (usually a {@code Long} or {@code String} to
 * an instance of {@link EntityClassAwareId}.
 *
 * @param <S> Value's type to convert from
 * @param <T> {@code EntityClassAwareId}'s descendant to convert to
 */
abstract class AbstractEntityClassAwareIdConverter
    <S extends Serializable, T extends EntityClassAwareId<?>> implements Converter<S, T> {

  private final Constructor<T> targetConstructor;

  AbstractEntityClassAwareIdConverter(Class<T> targetClass) {
    targetConstructor = findConstructor(targetClass);
  }

  @Override
  public T convert(S source) {
    return Optional.ofNullable(source)
        .map(it -> instantiateTarget(targetConstructor, preProcessSource(it)))
        .orElse(null);
  }

  protected abstract Serializable preProcessSource(S source);

  private Constructor<T> findConstructor(Class<T> targetClass) {
    Constructor<T> constructor = Optional
        .ofNullable(getConstructorIfAvailable(targetClass, long.class))
        .orElseGet(() -> getConstructorIfAvailable(targetClass, Long.class));

    return Optional
        .ofNullable(constructor)
        .orElseThrow(() -> new IllegalArgumentException("No constructor found"));
  }

  private T instantiateTarget(Constructor<T> targetConstructor, Serializable source) {
    return instantiateClass(targetConstructor, source);
  }

}
