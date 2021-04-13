package clerk.util;

import clerk.DataProcessor;
import clerk.collectors.FixedPeriodCollector;
import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/** A clerk that concurrently collects data at a fixed period. */
public class FixedPeriodClerk<O> extends SimpleClerk<O> {
  public <I> FixedPeriodClerk(
      Supplier<I> source,
      DataProcessor<I, O> processor,
      ScheduledExecutorService executor,
      Duration period) {
    super(source, processor, new FixedPeriodCollector(executor, period));
  }

  public <I> FixedPeriodClerk(
      Collection<Supplier<I>> sources,
      DataProcessor<I, O> processor,
      ScheduledExecutorService executor,
      Duration period) {
    super(sources, processor, new FixedPeriodCollector(executor, period));
  }
}
