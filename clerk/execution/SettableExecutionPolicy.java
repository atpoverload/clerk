package clerk.execution;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;

/** Execution policy that schedules a provided runnable to run at a settable period. */
public final class SettableExecutionPolicy implements ExecutionPolicy {
  private Duration period = Duration.ZERO;

  public SettableExecutionPolicy() {}

  public void setPeriod(Duration period) {
    this.period = period;
  }

  @Override
  public void execute(Runnable r, ScheduledExecutorService executor) {
    Instant start = Instant.now();
    r.run();
    Duration rescheduleTime = period.minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toMillis() > 0) {
      executor.schedule(() -> execute(r, executor), rescheduleTime.toMillis(), MILLISECONDS);
    } else if (rescheduleTime.toNanos() > 0) {
      executor.schedule(() -> execute(r, executor), rescheduleTime.toNanos(), NANOSECONDS);
    } else {
      executor.execute(() -> execute(r, executor));
    }
  }
}
