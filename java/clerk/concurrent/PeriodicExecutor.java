package clerk.concurrent;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

/** Executor that schedules runnables at a fixed period. */
public final class PeriodicExecutor implements Executor {
  /** Runs a workload and then reschedules it to run at the next interval. */
  public static void runAndReschedule(
      Runnable r, ScheduledExecutorService executor, Duration period) {
    Instant start = Instant.now();
    r.run();
    Duration rescheduleTime = period.minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toMillis() > 0) {
      executor.schedule(
          () -> runAndReschedule(r, executor, period), rescheduleTime.toMillis(), MILLISECONDS);
    } else if (rescheduleTime.toNanos() > 0) {
      executor.schedule(
          () -> runAndReschedule(r, executor, period), rescheduleTime.toNanos(), NANOSECONDS);
    } else {
      executor.execute(() -> runAndReschedule(r, executor, period));
    }
  }

  private final ScheduledExecutorService executor;
  private final Duration period;

  public PeriodicExecutor(ScheduledExecutorService executor, Duration period) {
    this.executor = executor;
    this.period = period;
  }

  @Override
  public final void execute(Runnable r) {
    executor.execute(() -> runAndReschedule(r, executor, period));
  }
}
