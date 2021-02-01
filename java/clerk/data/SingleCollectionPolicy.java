package clerk.data;

import clerk.Clerk;
import clerk.CollectionPolicy;
import clerk.Processor;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/** Policy that collects data once. */
public final class SingleCollectionPolicy implements CollectionPolicy {
  private final ExecutorService executor;
  private final ArrayList<Future<?>> dataFutures = new ArrayList<>();

  public SingleCollectionPolicy(ExecutorService executor) {
    this.executor = executor;
  }

  /** Execute the runnable once. */
  @Override
  public void start(Supplier<?> source, Processor<?, ?> processor) {
    dataFutures.add(executor.submit(() -> Clerk.pipe(source, processor)));
  }

  @Override
  public void stop() {
    // make sure the previous futures are done or cancelled
    for (Future<?> future : dataFutures) {
      // attempt to cancel the future; if we can't, get the result safely
      if (!future.isDone() && !future.isCancelled() && !future.cancel(false)) {
        try {
          future.get();
        } catch (Exception e) {
          System.out.println("could not consume a data future");
          e.printStackTrace();
        }
      }
    }
    dataFutures.clear();
  }
}
