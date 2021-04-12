package clerk.util;

import clerk.DataProcessor;
import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/** A clerk that concurrently collects data at a fixed period. */
public class FixedPeriodClerk<O> extends SimpleClerk<O> {
  public FixedPeriodClerk(
      Supplier<?> source,
      DataProcessor<?, O> processor,
      ScheduledExecutorService executor,
      Duration period) {
    super(source, processor, new FixedPeriodCollector(executor, period));
  }

  public FixedPeriodClerk(
      Collection<Supplier<?>> sources,
      DataProcessor<?, O> processor,
      ScheduledExecutorService executor,
      Duration period) {
    super(sources, processor, new FixedPeriodCollector(executor, period));
  }
}
