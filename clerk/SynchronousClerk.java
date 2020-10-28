package clerk;

import clerk.util.ClerkLogger;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * A clerk that collects data on {@code start()}, {@code stop()}, and {@code read()} depending on if
 * it is running.
 *
 * <p>NOTE: Source and processor data operations are called by the API calling thread.
 */
// TODO(timurbey): it may be better to have a separate executor for the data operations and
// either have the caller block on {@code read()} or change to a future.
public final class SynchronousClerk<O> implements Clerk<O> {
  private static final Logger logger = ClerkLogger.getLogger();

  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;

  private boolean isRunning = false;

  public SynchronousClerk(Iterable<Supplier<?>> sources, Processor<?, O> processor) {
    this.sources = sources;
    this.processor = processor;
  }

  /**
   * Grabs a sample from each data source and puts it into the processor.
   *
   * <p>NOTE: the profiler will report a warning if invoked while running.
   */
  @Override
  public void start() {
    if (!isRunning) {
      pipeData();
      isRunning = true;
    } else {
      logger.warning("clerk was told to start while running!");
    }
  }

  /**
   * Grabs a sample from each data source and puts them into the processor.
   *
   * <p>NOTE: the profiler will report a warning if invoked while not running.
   */
  @Override
  public void stop() {
    if (isRunning) {
      pipeData();
      isRunning = false;
    } else {
      logger.warning("clerk was told to stop while not running!");
    }
  }

  /** Grabs a sample from each data source, puts them into the processor, and return the result. */
  @Override
  public O read() {
    if (isRunning) {
      pipeData();
    }
    return processor.process();
  }

  private void pipeData() {
    for (Supplier<?> source : sources) {
      Clerk.pipe(source, processor);
    }
  }
}
