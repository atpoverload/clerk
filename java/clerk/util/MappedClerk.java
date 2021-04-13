package clerk.util;

import clerk.Clerk;
import clerk.DataCollector;
import clerk.DataProcessor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/** A Clerk that maps sources to collectors. */
public class MappedClerk<O> implements Clerk<O> {
  private final Map<Supplier<? extends Object>, DataCollector> sources;
  private final DataProcessor<? extends Object, O> processor;

  private boolean isRunning = false;

  public <I> MappedClerk(
      Map<Supplier<? extends Object>, DataCollector> sources,
      DataProcessor<? extends Object, O> processor) {
    this.sources = sources;
    this.processor = processor;
  }

  /** Starts each source-collector pair. */
  @Override
  public final void start() {
    if (!isRunning) {
      isRunning = true;
      for (Supplier<?> key : sources.keySet()) {
        collectData(key, processor);
      }
    }
  }

  /** Stops collection. */
  @Override
  public final void stop() {
    if (isRunning) {
      for (DataCollector collector : sources.values()) {
        collector.stop();
      }
      isRunning = false;
    }
  }

  /** Return the processor's output. */
  @Override
  public final O read() {
    return processor.process();
  }

  private <I> void collectData(Supplier<?> source, DataProcessor<I, ?> processor) {
    sources.get(source).collect((Supplier<I>) source, processor);
  }

  /** Helper class as an alternative to building the map directly. */
  // TODO(timur): this is not type-safe
  public static final class Builder<I, O> {
    private final HashMap<Supplier<?>, DataCollector> sources = new HashMap();

    private DataProcessor<?, O> processor;

    public Builder<I, O> addSource(Supplier<I> source, DataCollector collector) {
      this.sources.put(source, collector);
      return this;
    }

    public Builder<I, O> setDataProcessor(DataProcessor<I, O> processor) {
      this.processor = processor;
      return this;
    }

    public MappedClerk<O> build() {
      return new MappedClerk<O>(sources, processor);
    }
  }
}
