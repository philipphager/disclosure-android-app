package de.philipphager.disclosure.util.time;

import javax.inject.Inject;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

public class Stopwatch {
  private static final int MILLIS = 1000;
  private final Clock clock;
  private LocalDateTime startedAt;
  private Duration duration;

  @Inject public Stopwatch(Clock clock) {
    this.clock = clock;
  }

  public void start() {
    startedAt = clock.now();
  }

  public void stop() {
    ensureNotNull(startedAt, "called stop() before start()");
    LocalDateTime endedAt = clock.now();
    duration = Duration.between(startedAt, endedAt);
    startedAt = null;
  }

  public Duration getDuration() {
    return duration;
  }

  private double toSecs() {
    return duration.toMillis() / MILLIS;
  }

  @Override public String toString() {
    return String.format("duration %s sec.", toSecs());
  }
}
