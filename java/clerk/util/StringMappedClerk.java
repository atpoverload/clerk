package clerk.util;

import clerk.Clerk;
import clerk.DataCollector;
import clerk.DataProcessor;
import java.util.Map;
import java.util.function.Supplier;

/** A Clerk that maps sources to collectors with a {@link String} key. */
public final class StringMappedClerk<O> implements Clerk<O> {
  private final Map<String, Supplier<? extends Object>> sources;
  private final DataProcessor<? extends Object, O> processor;
  private final Map<String, DataCollector> collectors;

  private boolean isRunning = false;

  public <I> StringMappedClerk(
      Map<String, Supplier<I>> sources,
      DataProcessor<I, O> processor,
      Map<String, DataCollector> collectors) {
    this.sources = Map.copyOf(sources);
    this.processor = processor;
    this.collectors = collectors;
  }

  /** Starts each source-collector pair. */
  @Override
  public final void start() {
    if (!isRunning) {
      isRunning = true;
      for (String key : collectors.keySet()) {
        collectData(key, processor);
      }
    }
  }

  /** Stops collection. */
  @Override
  public final void stop() {
    if (isRunning) {
      for (String key : collectors.keySet()) {
        collectors.get(key).stop();
      }
      isRunning = false;
    }
  }

  /** Return the processor's output. */
  @Override
  public final O read() {
    return processor.process();
  }

  private <I> void collectData(String key, DataProcessor<I, ?> processor) {
    collectors.get(key).collect((Supplier<I>) sources.get(key), processor);
  }
}
