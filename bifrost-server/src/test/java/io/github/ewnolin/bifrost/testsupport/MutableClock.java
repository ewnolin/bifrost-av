package io.github.ewnolin.bifrost.testsupport;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/** Test clock that stays still until a test moves it forward. */
public class MutableClock extends Clock {

  private Instant now;

  /**
   * Creates a clock fixed at {@code start}.
   *
   * @param start the initial time
   */
  public MutableClock(Instant start) {
    this.now = start;
  }

  /**
   * Moves the clock forward.
   *
   * @param duration how far to move
   */
  public void advance(Duration duration) {
    now = now.plus(duration);
  }

  @Override
  public Instant instant() {
    return now;
  }

  @Override
  public ZoneId getZone() {
    return ZoneOffset.UTC;
  }

  @Override
  public Clock withZone(ZoneId zone) {
    return this;
  }
}
