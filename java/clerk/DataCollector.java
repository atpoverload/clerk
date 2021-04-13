package clerk;

import java.util.function.Supplier;

/** Interface that connects a data source's output to a processor. */
public interface DataCollector {
  /** Starts collecting from a source. */
  <I> void collect(Supplier<I> source, DataProcessor<I, ?> processor);

  /** Stops all collection. */
  void stop();
}
