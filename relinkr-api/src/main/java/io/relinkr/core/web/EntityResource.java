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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Base class for DTOs returned by the controller layer for receiving and sending data. Supports
 * standard fields like {@code id}, {@code createdDate}, {@code lastModifiedDate} and {@code
 * version}.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class EntityResource {

  @JsonProperty("id")
  private final String resourceId;

  private final LocalDateTime createdDate;
  private final LocalDateTime lastModifiedDate;
  private final Integer version;

  /**
   * Creates a new DTO based on the given entity.
   *
   * @param entity Entity to create a DTO from
   */
  public static EntityResource of(@NonNull AbstractEntity entity) {
    String resourceId = Optional.ofNullable(entity.getId()).map(String::valueOf).orElse(null);
    LocalDateTime createdDate = entity.getCreatedDate();
    LocalDateTime lastModifiedDate = entity.getLastModifiedDate();
    Integer version = entity.getVersion();

    return new EntityResource(resourceId, createdDate, lastModifiedDate, version);
  }

  /**
   * Creates a new DTO based on {@code resourceId}, {@code createdDate}, {@code lastModifiedDate},
   * {@code version}.
   *
   * @param resourceId       Resource's ID
   * @param createdDate      Created Date
   * @param lastModifiedDate Date last modified
   * @param version          version
   */
  public static EntityResource of(
      String resourceId,
      LocalDateTime createdDate, LocalDateTime lastModifiedDate,
      Integer version) {

    return new EntityResource(resourceId, createdDate, lastModifiedDate, version);
  }

}
