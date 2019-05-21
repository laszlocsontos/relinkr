package io.relinkr.link.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@EqualsAndHashCode(of = "tagName")
@Getter
@ToString(of = "tagName")
public class Tag implements Serializable {

  private String tagName;

  public Tag(String tagName) {
    this.tagName = tagName;
  }

  /*
   * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
   */
  Tag() {
  }

}
