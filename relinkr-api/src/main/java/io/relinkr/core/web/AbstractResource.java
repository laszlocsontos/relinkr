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

package io.relinkr.core.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.relinkr.core.orm.AbstractEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

/**
 * Base class for DTOs returned by the controller layer for receiving and sending data. Supports
 * standard fields like {@code id}, {@code createdDate}, {@code lastModifiedDate}
 * and {@code version}.
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractResource extends ResourceSupport {

  @JsonProperty("id")
  private String resourceId;

  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;
  private Integer version;

  /**
   * Creates a new DTO based on the given entity.
   *
   * @param entity Entity to create a DTO from
   */
  protected AbstractResource(@NonNull AbstractEntity entity) {
    this.resourceId = Optional.ofNullable(entity.getId()).map(String::valueOf).orElse(null);
    this.createdDate = entity.getCreatedDate();
    this.lastModifiedDate = entity.getLastModifiedDate();
    this.version = entity.getVersion();
  }

}
