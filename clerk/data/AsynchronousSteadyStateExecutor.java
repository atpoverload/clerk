package clerk.data;

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
 * <p>This wraps around a ScheduledExecutorService and uses schedule() re-run the workload,
 * implementing sleepless, periodic execution. I prefer this to sleep-looping for the following
 * reasons: 1) Interrupts interfere with sleeping, breaking steady-state execution. 2) Looping
 * sleeps requires locking a single thread to a workload.
 *
 * <p>All submitted workloads are scheduled on the same period. We are working towards other
 * schedule management schemes. A crude workaround is to use an internal timer within a data source.
 * An example of this is shown in {@link DeferringSupplier}.
 *
 * <p>Termination of tasks is done through atomic flags to avoid shutting down the executor. This
 * allows it to be reusable so that we do not need to explicitly rebuild the clerk if we stop
 * sampling.
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

  /** Safely prevents creation of new tasks until all current tasks are dead. */
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
