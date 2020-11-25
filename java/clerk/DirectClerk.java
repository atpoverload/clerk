package clerk;

import clerk.util.ClerkUtil;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * A clerk that collects data on {@code start()} and {@code stop()}.
 *
 * <p>NOTE: Data operations are done on the calling thread.
 */
public class DirectClerk<O> implements Clerk<O> {
  private static final Logger logger = ClerkUtil.getLogger();

  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;

  private boolean isRunning;

  public DirectClerk(Supplier<?> source, Processor<?, O> processor) {
    this.sources = List.of(source);
    this.processor = processor;
  }

  public DirectClerk(Iterable<Supplier<?>> sources, Processor<?, O> processor) {
    this.sources = sources;
    this.processor = processor;
  }

  /**
   * Pipes data into the processor.
   *
   * <p>NOTE: the profiler will report a warning if started while running.
   */
  @Override
  public final void start() {
    if (!isRunning) {
      isRunning = true;
      startCollecting();
    } else {
      logger.warning("start called while running!");
    }
  }

  /**
   * Pipes data into the processor.
   *
   * <p>NOTE: the profiler will report a warning if stopped while not running.
   */
  @Override
  public final void stop() {
    if (isRunning) {
      startCollecting();
      isRunning = false;
    } else {
      logger.warning("stop called while not running!");
    }
  }

  /** Return the processor's output. */
  @Override
  public final O read() {
    return processor.process();
  }

  /** Pipes data from the sources to the processor on the calling thread. */
  private void startCollecting() {
    for (Supplier<?> source : sources) {
      Clerk.pipe(source, processor);
    }
  }
}
