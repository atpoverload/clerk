package clerk.concurrent;

import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;

public final class Scheduler {
  private final ScheduledExecutorService executor;

  @Inject
  Scheduler(ScheduledExecutorService executor) {
    this.executor = executor;
  }

  public void schedule(Runnable r, long period) {
    executor.execute(() -> runAndReschedule(r, period));
  }

  public void stop() {
    try {
      executor.shutdownNow();
      while (!executor.awaitTermination(250, MILLISECONDS)) { }
    } catch (InterruptedException e) { }
  }

  private void runAndReschedule(Runnable r, long period) {
    long start = System.nanoTime();

    r.run();

    long elapsed = System.nanoTime() - start;

    long millis = elapsed / 1000000;
    int nanos = (int)(elapsed - millis * 1000000);

    millis = period - millis - (nanos > 0 ? 1 : 0);
    nanos = min(1000000 - nanos, 999999);

    if (millis >= 0 && nanos > 0) {
      executor.schedule(() -> runAndReschedule(r, period), millis, MILLISECONDS);
    }
  }
}
