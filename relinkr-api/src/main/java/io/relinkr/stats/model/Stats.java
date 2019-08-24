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

package io.relinkr.stats.model;

import static io.relinkr.stats.model.Stats.StatType.CLICKS;
import static io.relinkr.stats.model.Stats.StatType.LINKS;
import static io.relinkr.stats.model.Stats.StatType.VISITORS;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class Stats<K> {

  private final StatType type;
  private final Collection<StatEntry<K>> entries;

  private final TimeSpan currentTimeSpan;
  private final Collection<TimeSpan> availableTimeSpans;

  /**
   * Creates link statistics keyed by date.
   *
   * @param entries {@link Stats} entries
   * @param currentTimeSpan Current {@link TimeSpan}
   * @param availableTimeSpans Other available {@link TimeSpan}s
   * @return link statistics
   */
  public static Stats<LocalDate> ofLinks(
      Collection<StatEntry<LocalDate>> entries, TimeSpan currentTimeSpan,
      @NonNull Collection<TimeSpan> availableTimeSpans) {

    checkEntries(entries, currentTimeSpan);

    return new Stats<>(
        LINKS,
        orderBy(entries, Comparator.comparing(StatEntry::getKey)),
        currentTimeSpan, availableTimeSpans
    );
  }

  /**
   * Creates click statistics keyed by date.
   *
   * @param entries {@link Stats} entries
   * @param currentTimeSpan Current {@link TimeSpan}
   * @param availableTimeSpans Other available {@link TimeSpan}s
   * @return click statistics
   */
  public static Stats<LocalDate> ofClicks(
      Collection<StatEntry<LocalDate>> entries, TimeSpan currentTimeSpan,
      @NonNull Collection<TimeSpan> availableTimeSpans) {

    checkEntries(entries, currentTimeSpan);

    return new Stats<>(
        CLICKS,
        orderBy(entries, Comparator.comparing(StatEntry::getKey)),
        currentTimeSpan, availableTimeSpans
    );
  }

  /**
   * Creates click statistics keyed by new vs. returning.
   *
   * @param entries {@link Stats} entries
   * @param currentTimeSpan Current {@link TimeSpan}
   * @param availableTimeSpans Other available {@link TimeSpan}s
   * @return click statistics
   */
  public static Stats<String> ofVisitors(
      @NonNull Collection<StatEntry<String>> entries, @NonNull TimeSpan currentTimeSpan,
      @NonNull Collection<TimeSpan> availableTimeSpans) {

    // TODO: Key could be an enum instead to ensure type safety instead of a string

    return new Stats<>(
        VISITORS,
        orderBy(entries, Comparator.comparing(StatEntry::getValue)),
        currentTimeSpan, availableTimeSpans
    );
  }

  private static void checkEntries(
      @NonNull Collection<StatEntry<LocalDate>> entries, @NonNull TimeSpan currentTimeSpan) {

    for (StatEntry<LocalDate> entry : entries) {
      if (!currentTimeSpan.contains(entry.getKey())) {
        throw new IllegalArgumentException(
            "Current timespan from " + currentTimeSpan.getStartDate().format(ISO_DATE)
                + " to " + currentTimeSpan.getEndDate().format(ISO_DATE)
                + " doesn't cover " + entry.getKey().format(ISO_DATE)
        );
      }
    }
  }

  private static <K> Collection<StatEntry<K>> orderBy(
      Collection<StatEntry<K>> entries, Comparator<? super StatEntry<K>> comparator) {

    return entries.stream().sorted(comparator).collect(toList());
  }

  public enum StatType {
    LINKS,
    CLICKS,
    VISITORS
  }

}
