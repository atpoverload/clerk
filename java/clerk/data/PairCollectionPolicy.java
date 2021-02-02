package clerk.data;

import clerk.Clerk;
import clerk.Processor;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/** Policy that collects data once. */
public final class PairCollectionPolicy extends ConcurrentCollectionPolicy {
  private final ArrayList<Runnable> collectors = new ArrayList<>();

  private boolean isRunning;

  public PairCollectionPolicy(ScheduledExecutorService executor) {
    super(executor);
  }

  /** Check for futures from a previous collection, then store and collect. */
  @Override
  public void collect(Supplier<?> source, Processor<?, ?> processor) {
    synchronized (collectors) {
      if (!isRunning) {
        stopFutures();
        collectors.clear();
      }
      Runnable collector = () -> Clerk.pipe(source, processor);
      collectors.add(collector);
      addFuture(executor.submit(collector));
      isRunning = true;
    }
  }

  /** Collect from all collectors and then discard them. */
  @Override
  public void stop() {
    synchronized (collectors) {
      if (isRunning) {
        for (Runnable collector : collectors) {
          addFuture(executor.submit(collector));
        }
        collectors.clear();
        isRunning = false;
      }
    }
  }
}
