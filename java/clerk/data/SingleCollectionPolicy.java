package clerk.data;

import clerk.Clerk;
import clerk.CollectionPolicy;
import clerk.Processor;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/** Policy that collects data once. */
public final class SingleCollectionPolicy implements CollectionPolicy {
  private final ExecutorService executor;

  public SingleCollectionPolicy(ExecutorService executor) {
    this.executor = executor;
  }

  /** Execute the runnable once. */
  @Override
  public void start(Supplier<?> source, Processor<?, ?> processor) {
    executor.submit(() -> Clerk.pipe(source, processor));
  }

  @Override
  public void stop() {}
}
