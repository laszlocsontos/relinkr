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

package io.relinkr.stats.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.relinkr.stats.model.StatEntry;
import io.relinkr.stats.model.Stats;
import io.relinkr.stats.model.TimeSpan;
import java.util.Collection;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;

@Getter
@SuppressFBWarnings(
    value = {"EQ_DOESNT_OVERRIDE_EQUALS"},
    justification = "Overriding equals() wouldn't contribute to the class' identity"
)
class StatsResource<K> extends Resources<StatEntry<K>> {

  @JsonProperty("timespan")
  private final TimeSpan timeSpan;

  private StatsResource(Collection<StatEntry<K>> entries, TimeSpan timeSpan, Link... links) {
    super(entries, links);
    this.timeSpan = timeSpan;
  }

  static <K> StatsResource of(Stats<K> stats, Link... links) {
    return new StatsResource<>(stats.getEntries(), stats.getCurrentTimeSpan(), links);
  }

}
