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

package io.relinkr.user.model;

import static io.relinkr.core.model.TimeZone.UTC;
import static java.util.Locale.ENGLISH;
import static lombok.AccessLevel.PACKAGE;

import io.relinkr.core.model.TimeZone;
import java.io.Serializable;
import java.util.Locale;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * TODO: Implement timezone handling and proper internationalization, so that users see timestamps
 *    in their own local timezone.
 */
@Embeddable
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString(of = {"timeZone", "locale"})
public class UserPreferences implements Serializable {

  private static final long serialVersionUID = 5866970614995291299L;

  public static final UserPreferences DEFAULT = UserPreferences.of(UTC, ENGLISH);

  @NonNull
  private TimeZone timeZone = UTC;

  @NonNull
  private Locale locale = ENGLISH;

}
