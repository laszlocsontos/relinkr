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

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA configuration; it enabled auditing, that is, setting the
 * {@link AbstractEntity#getCreatedDate()} and {@link AbstractEntity#getLastModifiedDate()}
 * fields by utilizing {@link UtcLocalDateTimeProvider}.
 */
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "utcLocalDateTimeProvider")
public class JpaConfig {

}
