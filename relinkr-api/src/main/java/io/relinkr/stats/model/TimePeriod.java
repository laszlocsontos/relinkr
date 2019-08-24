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

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;

import java.time.LocalDate;

public enum TimePeriod {

  TODAY {
    @Override
    TimeSpan toTimeSpan(LocalDate today) {
      return TimeSpan.of(this, today, today);
    }

  },

  YESTERDAY {
    @Override
    TimeSpan toTimeSpan(LocalDate today) {
      LocalDate yesterday = today.minusDays(1);
      return TimeSpan.of(this, yesterday, yesterday);
    }

  },

  THIS_WEEK {
    @Override
    TimeSpan toTimeSpan(LocalDate today) {
      LocalDate startDate = today.with(DAY_OF_WEEK, MONDAY.getValue());
      LocalDate endDate = today.with(DAY_OF_WEEK, SUNDAY.getValue());
      return TimeSpan.of(this, startDate, endDate);
    }

  },

  PAST_WEEK {
    @Override
    TimeSpan toTimeSpan(LocalDate today) {
      LocalDate startDate = today.with(DAY_OF_WEEK, MONDAY.getValue()).minusWeeks(1);
      LocalDate endDate = today.with(DAY_OF_WEEK, SUNDAY.getValue()).minusWeeks(1);
      return TimeSpan.of(this, startDate, endDate);
    }

  },

  THIS_MONTH {
    @Override
    TimeSpan toTimeSpan(LocalDate today) {
      LocalDate startDate = today.withDayOfMonth(1);
      LocalDate endDate = today.plusMonths(1).withDayOfMonth(1).minusDays(1);
      return TimeSpan.of(this, startDate, endDate);
    }

  },

  PAST_MONTH {
    @Override
    TimeSpan toTimeSpan(LocalDate today) {
      LocalDate startDate = today.minusMonths(1).withDayOfMonth(1);
      LocalDate endDate = today.withDayOfMonth(1).minusDays(1);
      return TimeSpan.of(this, startDate, endDate);
    }

  },

  THIS_YEAR {
    @Override
    TimeSpan toTimeSpan(LocalDate today) {
      LocalDate startDate = today.withDayOfYear(1);
      LocalDate endDate = today.plusYears(1).withDayOfYear(1).minusDays(1);
      return TimeSpan.of(this, startDate, endDate);
    }

  },

  PAST_YEAR {
    @Override
    TimeSpan toTimeSpan(LocalDate today) {
      LocalDate startDate = today.minusYears(1).withDayOfYear(1);
      LocalDate endDate = today.withDayOfYear(1).minusDays(1);
      return TimeSpan.of(this, startDate, endDate);
    }

  },

  CUSTOM {
    @Override
    TimeSpan toTimeSpan(LocalDate today) {
      throw new UnsupportedOperationException("Custom TimeSpan cannot be converted to TimeSpan");
    }
  };

  abstract TimeSpan toTimeSpan(LocalDate today);

}
