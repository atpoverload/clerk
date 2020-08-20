package clerk.concurrent;

import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.ScheduledExecutorService;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;

public final class Scheduler {
  private final ScheduledExecutorService executor;

  @Inject
  Scheduler(ScheduledExecutorService executor) {
    this.executor = executor;
  }

  public void schedule(Runnable r, Duration period) {
    executor.execute(() -> runAndReschedule(r, period));
  }

  public void cancel() {
    executor.shutdown();
  }

  private void runAndReschedule(Runnable r, Duration period) {
    Instant start = Instant.now();
    r.run();
    Duration sleepTime = period.minus(Duration.between(start, Instant.now()));

    if (sleepTime.toMillis() > 0) {
      executor.schedule(() -> runAndReschedule(r, period), sleepTime.toMillis(), MILLISECONDS);
    } else if (sleepTime.toNanos() > 0) {
      executor.schedule(() -> runAndReschedule(r, period), sleepTime.toNanos(), NANOSECONDS);
    } else {
      executor.execute(() -> runAndReschedule(r, period));
    }
  }
}
