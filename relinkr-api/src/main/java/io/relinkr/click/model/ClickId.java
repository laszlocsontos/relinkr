package io.relinkr.click.model;

import static lombok.AccessLevel.PACKAGE;

import io.relinkr.core.orm.AbstractId;
import javax.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PACKAGE)
public class ClickId extends AbstractId<Click> {

  public ClickId(long id) {
    super(id);
  }

  public static ClickId of(long id) {
    return new ClickId(id);
  }

  @Override
  public Class<Click> getEntityClass() {
    return Click.class;
  }

}
