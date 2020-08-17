package chappie.concurrent;

import java.time.Instant;

public final class ScheduleRecord {
  private final Instant start;
  private final Instant workEnd;
  private final Instant sleepEnd;

  ScheduleRecord(Instant start, Instant workEnd, Instant sleepEnd) {
    this.start = start;
    this.workEnd = workEnd;
    this.sleepEnd = sleepEnd;
  }
}
