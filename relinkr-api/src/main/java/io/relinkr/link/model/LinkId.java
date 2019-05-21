package io.relinkr.link.model;

import static lombok.AccessLevel.PROTECTED;

import io.relinkr.core.orm.AbstractId;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
public class LinkId extends AbstractId<Link> {

  public LinkId(long id) {
    super(id);
  }

  public static LinkId of(long id) {
    return new LinkId(id);
  }

  @Override
  public Class<Link> getEntityClass() {
    return Link.class;
  }

}
