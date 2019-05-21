package io.relinkr.click.service;

import static io.relinkr.test.Mocks.CLICK_ID;
import static io.relinkr.test.Mocks.CLICK_ID_ZERO;
import static io.relinkr.test.Mocks.LINK_ID;
import static io.relinkr.test.Mocks.TIMESTAMP;
import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.VISITOR_ID;
import static io.relinkr.test.Mocks.VISITOR_IP;

import io.relinkr.click.model.Click;
import io.relinkr.click.model.ClickId;
import io.relinkr.test.orm.BaseRepositoryTest;

public class ClickRepositoryTest extends BaseRepositoryTest<Click, ClickId, ClickRepository> {

  @Override
  protected Click createEntity() {
    return Click.of(LINK_ID, VISITOR_ID, USER_ID, VISITOR_IP, TIMESTAMP);
  }

  @Override
  protected ClickId getId() {
    return CLICK_ID;
  }

  @Override
  protected ClickId getNonExistentId() {
    return CLICK_ID_ZERO;
  }

}
