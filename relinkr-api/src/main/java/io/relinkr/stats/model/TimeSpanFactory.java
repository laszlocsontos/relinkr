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
import static io.relinkr.stats.model.TimePeriod.PAST_WEEK;
import static io.relinkr.stats.model.TimePeriod.THIS_WEEK;
import static io.relinkr.stats.model.TimePeriod.TODAY;
import static io.relinkr.stats.model.TimePeriod.YESTERDAY;

import java.time.Clock;
import java.time.LocalDate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimeSpanFactory {

  private final Clock clock;

  @Autowired
  public TimeSpanFactory(ObjectProvider<Clock> clockProvider) {
    this(clockProvider.getIfAvailable(Clock::systemUTC));
  }

  TimeSpanFactory(Clock clock) {
    this.clock = clock;
  }

  public TimeSpan period(TimePeriod period) {
    if (CUSTOM.equals(period)) {
      throw new IllegalArgumentException("Unsupported period: " + period);
    }

    LocalDate today = LocalDate.now(clock);

    return period.toTimeSpan(today);
  }

  public TimeSpan today() {
    return period(TODAY);
  }

  public TimeSpan yesterday() {
    return period(YESTERDAY);
  }

  public TimeSpan thisWeek() {
    return period(THIS_WEEK);
  }

  public TimeSpan pastWeek() {
    return period(PAST_WEEK);
  }

}
