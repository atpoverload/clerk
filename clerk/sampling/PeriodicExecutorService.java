package clerk.concurrent;

import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.ScheduledExecutorService;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;

/** Scheduler that periodically runs tasks on a scheduled executor service. */
final class PeriodicTaskRunner implements TaskRunner {
  private final Duration period;
  private final ScheduledExecutorService executor;
  private final ArrayList<Future<?>> tasks = new ArrayList<>();

  @Inject
  PeriodicTaskRunner(@SchedulingPeriod Duration period, ScheduledExecutorService executor) {
    this.period = period;
    this.executor = executor;
  }

  /** Executes a task that will be rescheduled. */
  @Override
  public void start(Runnable r) {
    tasks.add(executor.submit(() -> runAndReschedule(r)));
  }

  /** Shuts down the underlying executor service. */
  @Override
  public void stop() {
    for (Future<?> task: tasks) {
      task.cancel(false);
    }
  }

  /** Runs the workload and then schedules it to run at the next period start. */
  // TODO(timur): discuss reschedule as close to N time units as possible vs
  //   reschedule at next multiple of N.
  private void runAndReschedule(Runnable r) {
    Instant start = Instant.now();
    r.run();
    Duration rescheduleTime = period.minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toMillis() > 0) {
      tasks.add(executor.schedule(() -> runAndReschedule(r), rescheduleTime.toMillis(), MILLISECONDS));
    } else if (rescheduleTime.toNanos() > 0) {
      tasks.add(executor.schedule(() -> runAndReschedule(r), rescheduleTime.toNanos(), NANOSECONDS));
    } else {
      tasks.add(executor.submit(() -> runAndReschedule(r)));
    }
  }
}
