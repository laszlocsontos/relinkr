package io.relinkr.stats.model;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

class Quarters implements TemporalUnit {

  static final Quarters INSTANCE = new Quarters();

  private static final String NAME = "Quarters";
  private static final Duration DURATION = Duration.ofSeconds(31556952L / 4);

  private Quarters() {
  }

  @Override
  public Duration getDuration() {
    return DURATION;
  }

  @Override
  public boolean isDurationEstimated() {
    return true;
  }

  @Override
  public boolean isDateBased() {
    return true;
  }

  @Override
  public boolean isTimeBased() {
    return false;
  }

  @Override
  public <R extends Temporal> R addTo(R temporal, long amount) {
    return (R) temporal.plus(amount, this);
  }

  @Override
  public long between(Temporal temporal1Inclusive, Temporal temporal2Exclusive) {
    return temporal1Inclusive.until(temporal2Exclusive, this);
  }

  @Override
  public String toString() {
    return NAME;
  }

}
