package clerk;

import clerk.util.ClerkLogger;
import java.util.function.Supplier;
import java.util.logging.Logger;

/** Manages a system that collects and processes data. */
public interface Clerk<O> {
  /**
   * Helper method that casts the data source to the processor's input type.
   *
   * <p>If the input type is incorrect at runtime, then the failure will be reported before
   * abandoning the workload.
   */
  // TODO(timurbey): this shouldn't be here but I don't know where to put it still. it should
  // probably be in a util
  static <I> void pipe(Supplier<?> source, Processor<I, ?> processor) {
    try {
      Object data = source.get();
      processor.add((I) data);
    } catch (ClassCastException e) {
      Logger logger = ClerkLogger.getLogger();
      logger.severe("data source " + source.getClass() + " was not the expected type:");
      logger.severe(e.getMessage().split("\\(")[0]);
      throw e;
    }
  }

  /** Starts collecting data. */
  public void start();

  /** Stops collecting data. */
  public void stop();

  /** Consumes the collected data. */
  public O read();
}
