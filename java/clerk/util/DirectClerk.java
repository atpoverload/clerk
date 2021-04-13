package clerk.util;

import clerk.DataProcessor;
import clerk.collectors.DirectCollector;
import java.util.Collection;
import java.util.function.Supplier;

/** A clerk that collects data when starting and stopping. */
public class DirectClerk<O> extends SimpleClerk<O> {
  public <I> DirectClerk(Supplier<I> source, DataProcessor<I, O> processor) {
    super(source, processor, new DirectCollector());
  }

  public <I> DirectClerk(Collection<Supplier<I>> sources, DataProcessor<I, O> processor) {
    super(sources, processor, new DirectCollector());
  }
}
