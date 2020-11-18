package clerk.concurrent;

import static clerk.util.ClerkUtil.runAndReschedule;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

/** Executor that schedules runnables at a steady period. */
public final class PeriodicExecutor implements Executor {
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
