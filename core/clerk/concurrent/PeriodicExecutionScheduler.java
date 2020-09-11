package clerk.concurrent;

import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.ScheduledExecutorService;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;

/** Scheduler that periodically runs tasks on an scheduled executor service. */
final class PeriodicExecutionScheduler implements Scheduler {
  private final ScheduledExecutorService executor;
  private final Duration period;

  @Inject
  PeriodicExecutionScheduler(@PeriodicSchedulingRate Duration period, ScheduledExecutorService executor) {
    this.period = period;
    this.executor = executor;
  }

  /** Starts a task that will be rescheduled. */
  @Override
  public void schedule(Runnable r) {
    executor.execute(() -> runAndReschedule(r));
  }

  /** Terminates all running threads. */
  @Override
  public void cancel() {
    executor.shutdown();
  }

  /** Runs the workload and then schedules it to run at the next period start. */
  private void runAndReschedule(Runnable r) {
    Instant start = Instant.now();
    r.run();
    Duration rescheduleTime = period.minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toMillis() > 0) {
      executor.schedule(() -> runAndReschedule(r), rescheduleTime.toMillis(), MILLISECONDS);
    } else if (rescheduleTime.toNanos() > 0) {
      executor.schedule(() -> runAndReschedule(r), rescheduleTime.toNanos(), NANOSECONDS);
    } else {
      executor.execute(() -> runAndReschedule(r));
    }
  }
}
