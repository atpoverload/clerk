package clerk.util;

import clerk.Clerk;
import clerk.DataCollector;
import clerk.DataProcessor;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/** A clerk that uses the same processor and collector for all sources. */
public class SimpleClerk<O> implements Clerk<O> {
  private final Collection<Supplier<? extends Object>> sources;
  private final DataProcessor<? extends Object, O> processor;
  private final DataCollector collector;

  private boolean isRunning;

  public <I> SimpleClerk(
      Supplier<I> source, DataProcessor<I, O> processor, DataCollector collector) {
    this.sources = List.of(source);
    this.processor = processor;
    this.collector = collector;
  }

  public <I> SimpleClerk(
      Collection<Supplier<I>> sources, DataProcessor<I, O> processor, DataCollector collector) {
    this.sources = List.copyOf(sources);
    this.processor = processor;
    this.collector = collector;
  }

  /** Starts collector from each source. */
  @Override
  public final void start() {
    if (!isRunning) {
      isRunning = true;
      for (Supplier<?> source : sources) {
        collectData(source, processor);
      }
    }
  }

  /** Stops collection. */
  @Override
  public final void stop() {
    if (isRunning) {
      collector.stop();
      isRunning = false;
    }
  }

  /** Return the processor's output. */
  @Override
  public final O read() {
    return processor.process();
  }

  private <I> void collectData(Supplier<?> source, DataProcessor<I, ?> processor) {
    collector.collect((Supplier<I>) source, processor);
  }
}
