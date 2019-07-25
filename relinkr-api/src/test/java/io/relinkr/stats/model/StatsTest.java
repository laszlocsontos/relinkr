package io.relinkr.stats.model;

import static org.junit.Assert.fail;

import org.junit.Test;

public class StatsTest {

  // ofLinks

  @Test(expected = IllegalArgumentException.class)
  public void givenNullEntries_whenOfLinks_thenIllegalArgumentException() {

  }

  @Test(expected = IllegalArgumentException.class)
  public void givenNullCurrentTimeSpan_whenOfLinks_thenIllegalArgumentException() {

  }

  @Test(expected = IllegalArgumentException.class)
  public void givenNullAvailableTimeSpans_whenOfLinks_thenIllegalArgumentException() {

  }

  @Test(expected = IllegalArgumentException.class)
  public void givenInvalidEntriesOrCurrentTimeSpan_whenOfLinks_thenIllegalArgumentException() {
    // The given list of statistics entries must precisely match the given current timespan; that is
    // for every given day there must be an enties and there must not be such entries which do not
    // overlap with the time span
  }

  @Test
  public void givenValidEntriesAndCurrentTimeSpan_whenOfLinks_thenOrderedAccordingToKey() {
    // In case of link and click statistics the data must be ordered according to the key (date)
    fail();
  }

  // ofClicks

  @Test(expected = IllegalArgumentException.class)
  public void givenNullEntries_whenOfClicks_thenIllegalArgumentException() {

  }

  @Test(expected = IllegalArgumentException.class)
  public void givenNullCurrentTimeSpan_whenOfClicks_thenIllegalArgumentException() {

  }

  @Test(expected = IllegalArgumentException.class)
  public void givenNullAvailableTimeSpans_whenOfClicks_thenIllegalArgumentException() {

  }

  @Test(expected = IllegalArgumentException.class)
  public void givenInvalidEntriesOrCurrentTimeSpan_whenOfClicks_thenIllegalArgumentException() {
    // The given list of statistics entries must precisely match the given current timespan; that is
    // for every given day there must be an enties and there must not be such entries which do not
    // overlap with the time span
  }

  @Test
  public void givenValidEntriesAndCurrentTimeSpan_whenOfClicks_thenOrderedAccordingToKey() {
    // In case of link and click statistics the data must be ordered according to the key (date)
    fail();
  }

  // ofVisitors

  @Test(expected = IllegalArgumentException.class)
  public void givenNullEntries_whenOfVisitors_thenIllegalArgumentException() {

  }

  @Test(expected = IllegalArgumentException.class)
  public void givenNullCurrentTimeSpan_whenOfVisitors_thenIllegalArgumentException() {

  }

  @Test(expected = IllegalArgumentException.class)
  public void givenNullAvailableTimeSpans_whenOfVisitors_thenIllegalArgumentException() {

  }

  @Test(expected = IllegalArgumentException.class)
  public void givenInvalidEntriesOrCurrentTimeSpan_whenOfVisitors_thenIllegalArgumentException() {
    // The given list of statistics entries must precisely match the given current timespan; that is
    // for every given day there must be an enties and there must not be such entries which do not
    // overlap with the time span
  }

  @Test
  public void givenValidEntriesAndCurrentTimeSpan_whenOfVisitors_thenOrderedAccordingToValue() {
    // In case of visitor statistics the data must be ordered according to the value
    fail();
  }

}
