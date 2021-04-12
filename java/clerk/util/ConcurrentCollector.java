package clerk.util;

import clerk.DataCollector;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** Collector that provides future management helper methods for safer concurrent collection. */
public abstract class ConcurrentCollector implements DataCollector {
  private final ArrayList<Future<?>> futures = new ArrayList<>();

  protected final ScheduledExecutorService executor;

  public ConcurrentCollector(ScheduledExecutorService executor) {
    this.executor = executor;
  }

  /** Submits a runnable and stores the future. */
  protected final void submit(Runnable r) {
    synchronized (futures) {
      futures.add(executor.submit(r));
    }
  }

  /** Schedules a runnable and stores the future. */
  protected final void schedule(Runnable r, long time, TimeUnit unit) {
    synchronized (futures) {
      futures.add(executor.schedule(r, time, unit));
    }
  }

  /** Checks or forcibly ends all futures. */
  protected final void stopFutures() {
    synchronized (futures) {
      for (Future<?> future : futures) {
        // make sure previous futures are done or cancelled
        if (!future.isDone() && !future.isCancelled() && !future.cancel(false)) {
          try {
            future.get();
          } catch (Exception e) {
            System.out.println("could not consume a future");
            e.printStackTrace();
          }
        }
      }
      futures.clear();
    }
  }
}
