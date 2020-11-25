package clerk.data;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import clerk.CollectionPolicy;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

/** Policy that schedules runnables at a fixed period. */
public final class FixedPeriodPolicy implements CollectionPolicy {
  private final ScheduledExecutorService executor;
  private final Duration period;
  private final ArrayList<Future<?>> dataFutures = new ArrayList<>();

  private boolean isRunning = false;

  public FixedPeriodPolicy(ScheduledExecutorService executor, Duration period) {
    this.executor = executor;
    this.period = period;
  }

  /** Check for futures from a previous collection, then start collecting. */
  @Override
  public final void start(Runnable r) {
    if (isRunning != true) {
      checkFutures();
    }
    isRunning = true;
    dataFutures.add(executor.submit(() -> runAndReschedule(r)));
  }

  /** Stops collecting, which will terminate the futures without blocking. */
  @Override
  public final void stop() {
    isRunning = false;
  }

  private void checkFutures() {
    // make sure the previous futures are done or cancelled
    for (Future<?> future : dataFutures) {
      // attempt to cancel the future; if we can't, get the result safely
      if (!future.isDone() && !future.isCancelled() && !future.cancel(false)) {
        try {
          future.get();
        } catch (Exception e) {
          System.out.println("could not consume a data future");
          e.printStackTrace();
        }
      }
    }
    dataFutures.clear();
  }

  private void runAndReschedule(Runnable r) {
    if (!isRunning) {
      return;
    }

    Instant start = Instant.now();
    r.run();
    Duration rescheduleTime = period.minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toMillis() > 0) {
      dataFutures.add(
          executor.schedule(() -> runAndReschedule(r), rescheduleTime.toMillis(), MILLISECONDS));
    } else if (rescheduleTime.toNanos() > 0) {
      dataFutures.add(
          executor.schedule(() -> runAndReschedule(r), rescheduleTime.toNanos(), NANOSECONDS));
    } else {
      dataFutures.add(executor.submit(() -> runAndReschedule(r)));
    }
  }
}
