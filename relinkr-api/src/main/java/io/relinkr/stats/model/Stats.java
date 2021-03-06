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
import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class Stats<K> {

  private final StatType type;
  private final Collection<StatEntry<K>> entries;

  private final TimeSpan currentTimeSpan;

  /**
   * Creates link statistics keyed by date.
   *
   * @param entries         {@link Stats} entries
   * @param currentTimeSpan Current {@link TimeSpan}
   * @return link statistics
   */
  public static Stats<LocalDate> ofLinks(
      Collection<StatEntry<LocalDate>> entries, TimeSpan currentTimeSpan) {

    Collection<StatEntry<LocalDate>> sortedEntries = mergeAndSortEntries(entries, currentTimeSpan);

    return new Stats<>(
        LINKS,
        sortedEntries,
        currentTimeSpan
    );
  }

  /**
   * Creates click statistics keyed by date.
   *
   * @param entries         {@link Stats} entries
   * @param currentTimeSpan Current {@link TimeSpan}
   * @return click statistics
   */
  public static Stats<LocalDate> ofClicks(
      Collection<StatEntry<LocalDate>> entries, TimeSpan currentTimeSpan) {

    Collection<StatEntry<LocalDate>> sortedEntries = mergeAndSortEntries(entries, currentTimeSpan);

    return new Stats<>(
        CLICKS,
        sortedEntries,
        currentTimeSpan
    );
  }

  /**
   * Creates click statistics keyed by new vs. returning.
   *
   * @param entries         {@link Stats} entries
   * @param currentTimeSpan Current {@link TimeSpan}
   * @return click statistics
   */
  public static Stats<String> ofVisitors(
      @NonNull Collection<StatEntry<String>> entries, @NonNull TimeSpan currentTimeSpan) {

    // TODO: Key could be an enum instead to ensure type safety instead of a string

    return new Stats<>(
        VISITORS,
        orderBy(entries, Comparator.comparing(StatEntry::getValue)),
        currentTimeSpan
    );
  }

  private static Collection<StatEntry<LocalDate>> mergeAndSortEntries(
      @NonNull Collection<StatEntry<LocalDate>> entries, @NonNull TimeSpan currentTimeSpan) {

    entries.stream().collect(Collectors.toMap(StatEntry::getKey, Function.identity()));
    Map<LocalDate, StatEntry<LocalDate>> entryMap = new TreeMap<>();
    Set<LocalDate> allDates = currentTimeSpan.getAllDates();

    for (StatEntry<LocalDate> entry : entries) {
      if (!allDates.contains(entry.getKey())) {
        throw new IllegalArgumentException(
            "Current timespan from " + ISO_DATE.format(currentTimeSpan.getStartDate())
                + " to " + ISO_DATE.format(currentTimeSpan.getEndDate())
                + " doesn't cover " + ISO_DATE.format(entry.getKey())
        );
      }

      entryMap.put(entry.getKey(), entry);
    }

    // Add an entry with zero value for missing keys
    allDates.forEach(key -> entryMap.merge(
        key, StatEntry.of(key, 0), (oldValue, value) -> oldValue == null ? value : oldValue
    ));

    // Returns values ordered by the natural ordering of keys (date)
    return unmodifiableCollection(entryMap.values());
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
