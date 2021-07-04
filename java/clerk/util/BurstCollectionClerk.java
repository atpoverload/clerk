package clerk.util;

import clerk.DataProcessor;
import clerk.collectors.BurstCollector;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/** A clerk that concurrently collects data as quickly as possible. */
public class BurstCollectionClerk<O> extends SimpleClerk<O> {
  public <I> BurstCollectionClerk(
      Supplier<? extends I> source,
      DataProcessor<I, O> processor,
      ScheduledExecutorService executor) {
    super(source, processor, new BurstCollector(executor));
  }

  public <I> BurstCollectionClerk(
      Collection<Supplier<? extends I>> sources,
      DataProcessor<I, O> processor,
      ScheduledExecutorService executor) {
    super(sources, processor, new BurstCollector(executor));
  }
}
