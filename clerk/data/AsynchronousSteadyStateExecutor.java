package clerk.concurrent;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import clerk.ClerkComponent;
import clerk.ClerkExecutor;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;

/**
 * Executor that periodically runs submitted tasks.
 *
 * <p>This executor is a wrapper around a ScheduledExecutorService that uses schedule() to dispatch
 * work to some future time. Because schedule() is not necessarily executed on the calling thread,
 * this implements sleepless, periodic execution. This is preferred to sleep-looping threads for the
 * following reasons:
 *
 * <p>Interrupts interfere with sleeping, breaking steady-state execution.
 *
 * <p>Looping sleeps result in a single thread locked to a workload.
 *
 * <p>Termination of tasks are synchronized using atomics.
 */
final class AsynchronousSteadyStateExecutor implements ClerkExecutor {
  private final Duration period;
  private final ScheduledExecutorService executor;
  private final AtomicInteger tasks = new AtomicInteger(0);
  private final AtomicBoolean ready = new AtomicBoolean(true);

  @Inject
  AsynchronousSteadyStateExecutor(
      @ClerkComponent Duration period, @ClerkComponent ScheduledExecutorService executor) {
    this.period = period;
    this.executor = executor;
  }

  /**
   * Executes a task that will be rescheduled.
   *
   * <p>If the executor is in the process of stopping, new workloads will block until all old
   * workloads are done.
   */
  @Override
  public void start(Runnable r) {
    executor.execute(
        () -> {
          while (!ready.get()) {}
          executor.execute(() -> runAndReschedule(r));
          tasks.getAndIncrement();
        });
  }

  /** Safely prevents creation of new tasks. */
  @Override
  public void stop() {
    ready.set(false);
  }

  /**
   * Runs the workload and then schedules it to run at the next period start.
   *
   * <p>If the executor has been told to stop, no new tasks are created. When the final task
   * terminates, the executor is reset to ready.
   */
  private void runAndReschedule(Runnable r) {
    tasks.getAndDecrement();
    if (!ready.get()) {
      if (tasks.get() == 0) {
        ready.set(true);
      }
      return;
    }

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

    tasks.getAndIncrement();
  }
}
