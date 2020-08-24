package clerk.sampling;

import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import clerk.core.Scheduler;
import java.util.concurrent.ScheduledExecutorService;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;

/** Scheduler that periodically runs tasks on an scheduled executor service. */
public final class ExecutorScheduler implements Scheduler {
  private final ScheduledExecutorService executor;
  private final Duration period;

  @Inject
  Scheduler(@SamplingRate Duration period, ScheduledExecutorService executor) {
    this.period = period;
    this.executor = executor;
  }

  /** Starts a task that will be rescheduled. */
  public void schedule(Runnable r, Duration period) {
    executor.execute(() -> runAndReschedule(r, period));
  }

  /** Terminates all running threads. */
  public void cancel() {
    executor.shutdown();
  }

  /** Runs the workload and then schedules it to run at the next period start. */
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
