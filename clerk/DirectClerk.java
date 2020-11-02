package clerk;

import clerk.util.ClerkUtil;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * A clerk that collects data on {@code start()}, {@code stop()}, and {@code read()} depending on if
 * it is running.
 *
 * <p>NOTE: Source and processor data operations are called by the API calling thread.
 */
public class DirectClerk<O> implements SimpleClerk<O> {
  private static final Logger logger = ClerkUtil.getLogger();

  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;

  private boolean isRunning = false;

  public DirectClerk(Iterable<Supplier<?>> sources, Processor<?, O> processor) {
    this.sources = sources;
    this.processor = processor;
  }

  /**
   * Puts a sample from each data source into the processor.
   *
   * <p>NOTE: the profiler will report a warning if invoked while running.
   */
  @Override
  public final void start() {
    if (!isRunning) {
      pipeData();
      isRunning = true;
    } else {
      logger.warning("clerk was told to start while running!");
    }
  }

  /**
   * Puts a sample from each data source into the processor.
   *
   * <p>NOTE: the profiler will report a warning if invoked while not running.
   */
  @Override
  public final void stop() {
    if (isRunning) {
      pipeData();
      isRunning = false;
    } else {
      logger.warning("clerk was told to stop while not running!");
    }
  }

  /** Puts a sample from each data source into the processor and returns the result. */
  @Override
  public final O read() {
    if (isRunning) {
      pipeData();
    }
    return processor.process();
  }

  private void pipeData() {
    for (Supplier<?> source : sources) {
      ClerkUtil.pipe(source, processor);
    }
  }
}
