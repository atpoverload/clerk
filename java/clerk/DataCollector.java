package clerk;

import java.util.function.Supplier;

/** Interface that connects a data source's output to a processor. */
public interface DataCollector {
  /** Starts collecting from a source. */
  <I> void collect(Supplier<I> source, DataProcessor<I, ?> processor);

  /** Stops all collection. */
  void stop();

  /** Error that should be used for failures in {@link collect()} and {@link stop()}. */
  public class CollectionError extends RuntimeException {
    public CollectionError(Exception e) {
      super("unable to collect data", e);
    }
  }
}
