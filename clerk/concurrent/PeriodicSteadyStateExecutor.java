package clerk.concurrent;

import static clerk.util.ClerkUtil.runAndReschedule;

import clerk.ClerkComponent;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;

/** Execution policy that schedules a runnable to at a steady period. */
public final class PeriodicSteadyStateExecutor implements Executor {
  private final ScheduledExecutorService executor;
  private final Duration period;

  @Inject
  public PeriodicSteadyStateExecutor(
      @ClerkComponent ScheduledExecutorService executor, @ClerkComponent Duration period) {
    this.executor = executor;
    this.period = period;
  }

  @Override
  public final void execute(Runnable r) {
    executor.execute(() -> runAndReschedule(r, executor, period));
  }
}
