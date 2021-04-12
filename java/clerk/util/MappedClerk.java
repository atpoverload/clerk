package clerk.util;

import static clerk.DataCollector.CollectionError;

import clerk.Clerk;
import clerk.DataCollector;
import clerk.DataProcessor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/** A Clerk that maps sources to collectors. */
public final class MappedClerk<O> implements Clerk<O> {
  private final Map<Supplier<?>, DataCollector> sources;
  private final DataProcessor<?, O> processor;

  private boolean isRunning = false;

  public MappedClerk(Map<Supplier<?>, DataCollector> sources, DataProcessor<?, O> processor) {
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
    try {
      sources.get(source).collect((Supplier<I>) source, processor);
    } catch (ClassCastException e) {
      throw new CollectionError(e);
    }
  }

  /** Helper class as an alternative to building the map directly. */
  public static final class Builder<O> {
    private final HashMap<Supplier<?>, DataCollector> sources = new HashMap();

    private DataProcessor<?, O> processor;

    public Builder addSource(Supplier<?> source, DataCollector collector) {
      this.sources.put(source, collector);
      return this;
    }

    public Builder setDataProcessor(DataProcessor<?, O> processor) {
      this.processor = processor;
      return this;
    }

    public MappedClerk<O> build() {
      return new MappedClerk<O>(sources, processor);
    }
  }
}
