package clerk.data;

import clerk.CollectionPolicy;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

/** Policy that can concurrently collect data safely. */
public abstract class ConcurrentCollectionPolicy implements CollectionPolicy {
  private final ArrayList<Future<?>> futures = new ArrayList<>();

  protected final ScheduledExecutorService executor;

  public ConcurrentCollectionPolicy(ScheduledExecutorService executor) {
    this.executor = executor;
  }

  /** Adds a future to the synchronized storage. */
  protected final void addFuture(Future<?> future) {
    synchronized (futures) {
      futures.add(future);
    }
  }

  /** Checks or forcibly ends all stored futures. */
  protected final void stopFutures() {
    synchronized (futures) {
      // make sure the previous futures are done or cancelled
      for (Future<?> future : futures) {
        // attempt to cancel the future; if we can't, get the result safely
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
