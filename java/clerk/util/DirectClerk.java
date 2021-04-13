package clerk.util;

import clerk.DataProcessor;
import clerk.collectors.DirectCollector;
import java.util.Collection;
import java.util.function.Supplier;

/** A clerk that collects data when starting and stopping. */
public class DirectClerk<O> extends SimpleClerk<O> {
  public DirectClerk(Supplier<?> source, DataProcessor<?, O> processor) {
    super(source, processor, new DirectCollector());
  }

  public DirectClerk(Collection<Supplier<?>> sources, DataProcessor<?, O> processor) {
    super(sources, processor, new DirectCollector());
  }
}
