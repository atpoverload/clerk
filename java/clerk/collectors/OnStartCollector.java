package clerk.collectors;

import clerk.DataCollector;
import clerk.DataProcessor;
import java.util.function.Supplier;

/** Collector that collects data on the calling thread when starting or stopping the collector. */
public final class OnStartCollector implements DataCollector {
  /** Store the collector and run it. */
  @Override
  public <I> void collect(Supplier<I> source, DataProcessor<I, ?> processor) {
    synchronized (this) {
      processor.add(source.get());
    }
  }

  /** Run all stored collectors and discard them. */
  @Override
  public void stop() {}
}
