package io.relinkr.stats.model;

import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class TimeSpan {

  private final String name;
  private final LocalDate startDate;
  private final LocalDate endDate;

}
