package clerk.data;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import clerk.Clerk;
import clerk.Processor;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/** Policy that collects data at a fixed period. */
public final class FixedPeriodPolicy extends ConcurrentCollectionPolicy {
  private final Duration period;

  private boolean isRunning = false;

  public FixedPeriodPolicy(ScheduledExecutorService executor, Duration period) {
    super(executor);
    this.period = period;
  }

  /** Check for futures from a previous collection, then start collecting. */
  @Override
  public final void collect(Supplier<?> source, Processor<?, ?> processor) {
    if (isRunning != true) {
      stopFutures();
    }
    isRunning = true;
    addFuture(executor.submit(() -> runAndReschedule(() -> Clerk.pipe(source, processor))));
  }

  /** Stops collecting, which will terminate the futures without blocking. */
  @Override
  public final void stop() {
    isRunning = false;
  }

  private void runAndReschedule(Runnable r) {
    if (!isRunning) {
      return;
    }

    Instant start = Instant.now();
    r.run();
    Duration rescheduleTime = period.minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toNanos() > 0) {
      addFuture(
          executor.schedule(() -> runAndReschedule(r), rescheduleTime.toNanos(), NANOSECONDS));
    } else {
      addFuture(executor.submit(() -> runAndReschedule(r)));
    }
  }
}
