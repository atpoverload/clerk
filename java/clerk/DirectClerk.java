package clerk;

import clerk.util.ClerkUtil;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * A clerk that collects data on {@code start()} and {@code stop()}. {@code read()} will collect
 * data if the clerk is running.
 *
 * <p>NOTE: Source and processor data operations are called by the API calling thread.
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
      pipeData();
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
      pipeData();
      isRunning = false;
    } else {
      logger.warning("stop called while not running!");
    }
  }

  /**
   * Pipes data into the processor if the clerk is running, and then returns the processor's output.
   */
  @Override
  public final O read() {
    if (isRunning) {
      pipeData();
    }
    return processor.process();
  }

  /** Pipes data from the sources to the processor on the calling thread. */
  private void pipeData() {
    for (Supplier<?> source : sources) {
      Clerk.pipe(source, processor);
    }
  }
}
