package clerk;

import java.util.function.Supplier;

/** Interface for a data collector. */
public interface Clerk<O> {
  /** Casts the data source to the processor's input type and adds it. */
  public static <I> void pipe(Supplier<?> source, Processor<I, ?> processor) {
    processor.add((I) source.get());
  }

  /** Starts data collection. */
  void start();

  /** Stops data collection. */
  void stop();

  /** Returns the data. */
  O read();
}
