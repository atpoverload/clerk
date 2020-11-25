package clerk.data;

import clerk.CollectionPolicy;
import java.util.concurrent.Executor;

/** Policy that collects data once. */
public final class SingleCollectionPolicy implements CollectionPolicy {
  private final Executor executor;

  public SingleCollectionPolicy(Executor executor) {
    this.executor = executor;
  }

  /** Execute the runnable once. */
  @Override
  public final void start(Runnable r) {
    executor.execute(r);
  }

  @Override
  public final void stop() {}
}
