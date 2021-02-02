package clerk.data;

import clerk.Clerk;
import clerk.Processor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/** Policy that collects data once. */
public final class SingleCollectionPolicy extends ConcurrentCollectionPolicy {
  public SingleCollectionPolicy(ScheduledExecutorService executor) {
    super(executor);
  }

  /** Execute the runnable once and add the future. */
  @Override
  public void collect(Supplier<?> source, Processor<?, ?> processor) {
    addFuture(executor.submit(() -> Clerk.pipe(source, processor)));
  }

  /** Stop all futures. */
  @Override
  public void stop() {
    stopFutures();
  }
}
