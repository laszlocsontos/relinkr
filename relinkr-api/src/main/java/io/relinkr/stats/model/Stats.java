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
