package io.relinkr.click.model;

import static io.relinkr.test.Mocks.LINK_ID;
import static io.relinkr.test.Mocks.TIMESTAMP;
import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.VISITOR_ID;
import static io.relinkr.test.Mocks.VISITOR_IP;
import static org.junit.Assert.assertEquals;

import io.relinkr.core.model.Country;
import org.junit.Before;
import org.junit.Test;

public class ClickTest {

  private Click click;

  @Before
  public void setUp() throws Exception {
    click = Click.of(LINK_ID, VISITOR_ID, USER_ID, VISITOR_IP, TIMESTAMP);
  }

  @Test
  public void shouldHaveVisitDayOfMonth() {
    assertEquals(TIMESTAMP.getDayOfMonth(), click.getVisitDayOfMonth());
  }

  @Test
  public void shouldHaveVisitDayOfWeek() {
    assertEquals(TIMESTAMP.getDayOfWeek().getValue(), click.getVisitDayOfWeek());
  }

  @Test
  public void shouldHaveVisitHour() {
    assertEquals(TIMESTAMP.getHour(), click.getVisitHour());
  }

  @Test
  public void getVisitMonth() {
    assertEquals(TIMESTAMP.getMonth().getValue(), click.getVisitMonth());
  }

  @Test
  public void shouldHaveVisitTimestamp() {
    assertEquals(TIMESTAMP, click.getVisitTimestamp());
  }

  @Test
  public void shouldHaveCountry() {
    assertEquals(Country.US, click.with(Country.US).getCountry().get());
  }

}
