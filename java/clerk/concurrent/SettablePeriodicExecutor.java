package clerk.concurrent;

import static clerk.util.ClerkUtil.runAndReschedule;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

/** Executor that schedules runnables at a settable period. */
public final class SettablePeriodicExecutor implements Executor {
  private final ScheduledExecutorService executor;

  private Duration period;

  public SettablePeriodicExecutor(ScheduledExecutorService executor, Duration period) {
    this.executor = executor;
    this.period = period;
  }

  public void setPeriod(Duration period) {
    this.period = period;
  }

  public Duration getPeriod() {
    return period;
  }

  @Override
  public final void execute(Runnable r) {
    executor.execute(() -> runAndReschedule(r, executor, period));
  }
}
