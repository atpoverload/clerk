package clerk.scheduling;

import clerk.Scheduler;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public final class SteadyStateScheduler implements Scheduler {
  private final Supplier<Instant> timeSource;
  private final Duration period;

  public SteadyStateScheduler(Supplier<Instant> timeSource, Duration period) {
    this.timeSource = timeSource;
    this.period = period;
  }

  @Override
  public Duration getNextSchedulingTime(Supplier<?> source, Instant start) {
    return period.minus(Duration.between(start, timeSource.get()));
  }
}
