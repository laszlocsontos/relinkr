package io.relinkr.stats.model;

import static io.relinkr.stats.model.Stats.StatType.CLICKS;
import static io.relinkr.stats.model.Stats.StatType.LINKS;
import static io.relinkr.stats.model.Stats.StatType.VISITORS;
import static io.relinkr.test.Mocks.DATE_ENTRY_1;
import static io.relinkr.test.Mocks.DATE_ENTRY_2;
import static io.relinkr.test.Mocks.DATE_ENTRY_3;
import static io.relinkr.test.Mocks.DATE_ENTRY_4;
import static io.relinkr.test.Mocks.DATE_ENTRY_5;
import static io.relinkr.test.Mocks.DATE_ENTRY_6;
import static io.relinkr.test.Mocks.DATE_ENTRY_7;
import static io.relinkr.test.Mocks.ENTRIES_BY_DATE;
import static io.relinkr.test.Mocks.ENTRIES_BY_STRING;
import static io.relinkr.test.Mocks.STRING_ENTRY_1;
import static io.relinkr.test.Mocks.STRING_ENTRY_2;
import static io.relinkr.test.Mocks.TIME_SPAN;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StatsTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  // ofLinks

  @Test
  public void givenNullEntries_whenOfLinks_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("entries is marked @NonNull but is null");

    Stats.ofLinks(null, TIME_SPAN);
  }

  @Test
  public void givenNullCurrentTimeSpan_whenOfLinks_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("currentTimeSpan is marked @NonNull but is null");

    Stats.ofLinks(ENTRIES_BY_DATE, null);
  }

  @Test
  public void givenNullAvailableTimeSpans_whenOfLinks_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("availableTimeSpans is marked @NonNull but is null");

    Stats.ofLinks(null, TIME_SPAN);
  }

  @Test
  public void givenInvalidEntriesOrCurrentTimeSpan_whenOfLinks_thenIllegalArgumentException() {
    // The given list of statistics entries must precisely match the given current timespan; that is
    // for every given day there must be an entry and there must not be such entries which do not
    // overlap with the time span
    expectedException.expect(IllegalArgumentException.class);

    LocalDate extraDate = TIME_SPAN.getStartDate().minusDays(1);

    expectedException.expectMessage(
        "Current timespan from " + ISO_DATE.format(TIME_SPAN.getStartDate())
            + " to " + ISO_DATE.format(TIME_SPAN.getEndDate())
            + " doesn't cover " + ISO_DATE.format(extraDate)
    );

    Collection<StatEntry<LocalDate>> entries = new ArrayList<>(ENTRIES_BY_DATE);
    entries.add(StatEntry.of(extraDate, 1));

    Stats.ofLinks(entries, TIME_SPAN);
  }

  @Test
  public void givenValidEntriesAndCurrentTimeSpan_whenOfLinks_thenOrderedAccordingToKey() {
    // In case of link and click statistics the data must be ordered according to the key (date)
    Stats<LocalDate> stats = Stats.ofLinks(ENTRIES_BY_DATE, TIME_SPAN);

    assertEquals(LINKS, stats.getType());

    assertEquals(
        asList(
            DATE_ENTRY_1, DATE_ENTRY_2, DATE_ENTRY_3,
            DATE_ENTRY_4, DATE_ENTRY_5, DATE_ENTRY_6,
            DATE_ENTRY_7
        ),
        stats.getEntries()
    );
  }

  // ofClicks

  @Test
  public void givenNullEntries_whenOfClicks_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("entries is marked @NonNull but is null");

    Stats.ofClicks(null, TIME_SPAN);
  }

  @Test
  public void givenNullCurrentTimeSpan_whenOfClicks_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("currentTimeSpan is marked @NonNull but is null");

    Stats.ofClicks(ENTRIES_BY_DATE, null);
  }

  @Test
  public void givenNullAvailableTimeSpans_whenOfClicks_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("availableTimeSpans is marked @NonNull but is null");

    Stats.ofLinks(null, TIME_SPAN);
  }

  @Test
  public void givenInvalidEntriesOrCurrentTimeSpan_whenOfClicks_thenIllegalArgumentException() {
    // The given list of statistics entries must precisely match the given current timespan; that is
    // for every given day there must be an entry and there must not be such entries which do not
    // overlap with the time span
    expectedException.expect(IllegalArgumentException.class);

    LocalDate extraDate = TIME_SPAN.getStartDate().minusDays(1);

    expectedException.expectMessage(
        "Current timespan from " + ISO_DATE.format(TIME_SPAN.getStartDate())
            + " to " + ISO_DATE.format(TIME_SPAN.getEndDate())
            + " doesn't cover " + ISO_DATE.format(extraDate)
    );

    Collection<StatEntry<LocalDate>> entries = new ArrayList<>(ENTRIES_BY_DATE);
    entries.add(StatEntry.of(extraDate, 1));

    Stats.ofClicks(entries, TIME_SPAN);
  }

  @Test
  public void givenValidEntriesAndCurrentTimeSpan_whenOfClicks_thenOrderedAccordingToKey() {
    // In case of link and click statistics the data must be ordered according to the key (date)
    Stats<LocalDate> stats = Stats.ofClicks(ENTRIES_BY_DATE, TIME_SPAN);

    assertEquals(CLICKS, stats.getType());

    assertEquals(
        asList(
            DATE_ENTRY_1, DATE_ENTRY_2, DATE_ENTRY_3,
            DATE_ENTRY_4, DATE_ENTRY_5, DATE_ENTRY_6,
            DATE_ENTRY_7
        ),
        stats.getEntries()
    );
  }

  // ofVisitors

  @Test
  public void givenNullEntries_whenOfVisitors_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("entries is marked @NonNull but is null");

    Stats.ofVisitors(null, TIME_SPAN);
  }

  @Test
  public void givenNullCurrentTimeSpan_whenOfVisitors_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("currentTimeSpan is marked @NonNull but is null");

    Stats.ofVisitors(ENTRIES_BY_STRING, null);
  }

  @Test
  public void givenNullAvailableTimeSpans_whenOfVisitors_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("availableTimeSpans is marked @NonNull but is null");

    Stats.ofVisitors(ENTRIES_BY_STRING, TIME_SPAN);
  }

  @Test
  public void givenValidEntriesAndCurrentTimeSpan_whenOfVisitors_thenOrderedAccordingToValue() {
    // In case of visitor statistics the data must be ordered according to the value
    Stats<String> stats = Stats.ofVisitors(ENTRIES_BY_STRING, TIME_SPAN);

    assertEquals(VISITORS, stats.getType());
    assertEquals(asList(STRING_ENTRY_1, STRING_ENTRY_2), stats.getEntries());
  }

}
