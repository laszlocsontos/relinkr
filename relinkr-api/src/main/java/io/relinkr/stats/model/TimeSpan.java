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

import static io.relinkr.stats.model.TimePeriod.CUSTOM;
import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
@EqualsAndHashCode(of = "period, startDate, endDate")
public class TimeSpan {

  private final TimePeriod period;
  private final LocalDate startDate;
  private final LocalDate endDate;

  public Set<LocalDate> getAllDates() {
    Set<LocalDate> allDates = new LinkedHashSet<>();
    LocalDate date = startDate;
    while (!date.isAfter(endDate)) {
      allDates.add(date);
      date = date.plusDays(1);
    }
    return allDates;
  }

  static TimeSpan of(
      @NonNull TimePeriod period, @NonNull LocalDate startDate, @NonNull LocalDate endDate) {

    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("endDate cannot be earlier that startDate");
    }

    return new TimeSpan(period, startDate, endDate);
  }

  public static TimeSpan ofCustom(LocalDate startDate, LocalDate endDate) {
    return of(CUSTOM, startDate, endDate);
  }

}
