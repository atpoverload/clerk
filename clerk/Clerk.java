package clerk;

import clerk.util.ClerkLogger;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;
import javax.inject.Inject;

/** Manages a system that collects and processes data through a user API. */
public final class Clerk<O> {
  private static final Logger logger = ClerkLogger.createLogger();

  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final ClerkExecutor executor;

  private boolean isRunning = false;

  @Inject
  Clerk(
      @ClerkComponent Set<Supplier<?>> sources, Processor<?, O> processor, ClerkExecutor executor) {
    this.sources = sources;
    this.processor = processor;
    this.executor = executor;
  }

  /**
   * Feeds the output of the data sources into the processor.
   *
   * <p>NOTE: the profiler will ignore this call if it is running.
   */
  public void start() {
    if (!isRunning) {
      for (Supplier<?> source : sources) {
        executor.start(() -> pipe(source, processor));
      }
      isRunning = true;
    }
  }

  /**
   * Stops feedings data into the processor.
   *
   * <p>NOTE: the profiler will ignore this call if it is not running.
   */
  public void stop() {
    if (isRunning) {
      executor.stop();
      isRunning = false;
    }
  }

  /** Returns the output of the processor. */
  public O dump() {
    return processor.process();
  }

  /** Helper method that casts the input type. If the input type is . */
  private static <I> void pipe(Supplier<?> source, Processor<I, ?> processor) {
    Object o = source.get();
    try {
      processor.add((I) o);
    } catch (ClassCastException e) {
      logger.severe("data source " + source.getClass() + " was not the expected type:");
      logger.severe(e.getMessage().split("\\(")[0]);
      throw e;
    }
  }
}
