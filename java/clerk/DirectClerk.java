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
public class DirectClerk<O> {
  private static final Logger logger = ClerkUtil.getLogger();

  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;

  private boolean isRunning = false;

  public DirectClerk(Supplier<?> source, Processor<?, O> processor) {
    this.sources = List.of(source);
    this.processor = processor;
  }

  public DirectClerk(Iterable<Supplier<?>> sources, Processor<?, O> processor) {
    this.sources = sources;
    this.processor = processor;
  }

  /**
   * Puts a sample from each data source into the processor.
   *
   * <p>NOTE: the profiler will report a warning if started while running.
   */
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
   * <p>NOTE: the profiler will report a warning if stopped while not running.
   */
  public final void stop() {
    if (isRunning) {
      pipeData();
      isRunning = false;
    } else {
      logger.warning("clerk was told to stop while not running!");
    }
  }

  /**
   * Puts a sample from each data source into the processor if the clerk is running, and then
   * returns the processor's output.
   */
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
