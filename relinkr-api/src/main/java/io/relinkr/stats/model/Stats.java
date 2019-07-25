package io.relinkr.stats.model;

import static io.relinkr.stats.model.Stats.StatType.CLICKS;
import static io.relinkr.stats.model.Stats.StatType.LINKS;
import static io.relinkr.stats.model.Stats.StatType.VISITORS;
import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDate;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class Stats<K> {

  private final StatType type;
  private final Collection<StatEntry<K>> entries;

  private final TimeSpan currentTimeSpan;
  private final Collection<TimeSpan> availableTimeSpans;

  public static Stats<LocalDate> ofLinks(
      Collection<StatEntry<LocalDate>> entries, TimeSpan timeSpan,
      Collection<TimeSpan> availableTimeSpans) {

    return new Stats<>(LINKS, entries, timeSpan, availableTimeSpans);
  }

  public static Stats<LocalDate> ofClicks(
      Collection<StatEntry<LocalDate>> entries, TimeSpan timeSpan,
      Collection<TimeSpan> availableTimeSpans) {

    return new Stats<>(CLICKS, entries, timeSpan, availableTimeSpans);
  }

  public static Stats<String> ofVisitors(
      Collection<StatEntry<String>> entries, TimeSpan timeSpan,
      Collection<TimeSpan> availableTimeSpans) {

    return new Stats<>(VISITORS, entries, timeSpan, availableTimeSpans);
  }

  public enum StatType {
    LINKS,
    CLICKS,
    VISITORS
  }

}
