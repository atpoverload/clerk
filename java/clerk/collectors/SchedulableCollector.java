package clerk.collectors;

import clerk.DataCollector;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** Collector that provides future management helper methods for safer concurrent collection. */
public abstract class SchedulableCollector implements DataCollector {
  private final ArrayList<Future<?>> futures = new ArrayList<>();
  private final ScheduledExecutorService executor;

  private boolean isCollecting = false;

  public SchedulableCollector(ScheduledExecutorService executor) {
    this.executor = executor;
  }

  /** Submits a runnable and stores the future. */
  protected final void schedule(Runnable r) {
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

  /** Returns if the collector is collecting. */
  protected final boolean getCollectionState() {
    synchronized (this) {
      return isCollecting;
    }
  }

  /** Sets the collection state. If the state changes, all futures are stopped. */
  protected final void setCollectionState(boolean startCollecting) {
    synchronized (this) {
      if (isCollecting != startCollecting) {
        synchronized (futures) {
          for (Future<?> future : futures) {
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
        isCollecting = startCollecting;
      }
    }
  }
}
