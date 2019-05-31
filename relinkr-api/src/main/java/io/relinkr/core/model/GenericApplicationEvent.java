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

package io.relinkr.core.model;

import java.time.Instant;
import org.springframework.context.ApplicationEvent;

/**
 * Abstract base class of application level events; it differs from {@link ApplicationEvent} in two
 * ways. It's generic and accepts type parameter {@code S} and
 * {@link GenericApplicationEvent#getSource()} returns a value of that type. Other than that, it
 * also provides {@link GenericApplicationEvent#getInstant()} method for getting that point in time
 * when the event occurred. {@link ApplicationEvent} also has got
 * a {@link ApplicationEvent#getTimestamp()} method, but its value cannot be set freely.
 *
 * @param <S> Event source's type
 */
public abstract class GenericApplicationEvent<S> extends ApplicationEvent {

  private final Instant instant;

  public GenericApplicationEvent(S source, Instant instant) {
    super(source);
    this.instant = instant;
  }

  @Override
  public S getSource() {
    return (S) super.getSource();
  }

  public final Instant getInstant() {
    return instant;
  }

}
