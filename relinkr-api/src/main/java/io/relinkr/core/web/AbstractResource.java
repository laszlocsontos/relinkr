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
  public AbstractResource(@NonNull AbstractEntity entity) {
    this.resourceId = Optional.ofNullable(entity.getId()).map(String::valueOf).orElse(null);
    this.createdDate = entity.getCreatedDate();
    this.lastModifiedDate = entity.getLastModifiedDate();
    this.version = entity.getVersion();
  }

}
