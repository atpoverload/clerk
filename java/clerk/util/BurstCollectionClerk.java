package clerk.util;

import clerk.DataProcessor;
import clerk.collectors.BurstCollector;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/** A clerk that concurrently collects data at a fixed period. */
public class BurstCollectionClerk<O> extends SimpleClerk<O> {
  public BurstCollectionClerk(
      Supplier<?> source, DataProcessor<?, O> processor, ScheduledExecutorService executor) {
    super(source, processor, new BurstCollector(executor));
  }

  public BurstCollectionClerk(
      Collection<Supplier<?>> sources,
      DataProcessor<?, O> processor,
      ScheduledExecutorService executor) {
    super(sources, processor, new BurstCollector(executor));
  }
}
