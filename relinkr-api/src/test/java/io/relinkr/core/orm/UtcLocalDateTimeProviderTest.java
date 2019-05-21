package io.relinkr.core.orm;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import org.junit.Test;
import org.springframework.data.auditing.DateTimeProvider;

public class UtcLocalDateTimeProviderTest {

  private static final int OFFSET = 1;

  private DateTimeProvider dateTimeProvider = new UtcLocalDateTimeProvider();

  @Test
  public void getNow() {
    TemporalAccessor now = dateTimeProvider.getNow().get();
    TemporalAccessor nowWithOffset = LocalDateTime.now(ZoneOffset.ofHours(OFFSET));
    assertEquals(nowWithOffset.get(HOUR_OF_DAY) - OFFSET, now.get(HOUR_OF_DAY));
  }

}
