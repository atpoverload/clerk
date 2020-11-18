package clerk.clerks;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import clerk.Processor;
import clerk.util.ClerkUtil;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * A clerk that collects data on {@code start()} and {@code stop()}. {@code read()} will collect
 * data if the clerk is running.
 *
 * <p>NOTE: This is a non-blocking alternative to the {@link DirectClerk}.
 */
public class FutureClerk<O> {
  private static final Logger logger = ClerkUtil.getLogger();

  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final ExecutorService executor = newSingleThreadExecutor();
  private final Semaphore lock = new Semaphore(1);

  private boolean isRunning = false;

  public FutureClerk(Supplier<?> source, Processor<?, O> processor) {
    this.sources = List.of(source);
    this.processor = processor;
  }

  public FutureClerk(Iterable<Supplier<?>> sources, Processor<?, O> processor) {
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
   * <p>NOTE: the profiler will report a warning if stopped while running.
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
   * Returns a future that puts a sample from each data source into the processor if the clerk is
   * running and then returns the processor's output.
   */
  public final Future<O> read() {
    return executor.submit(
        () -> {
          try {
            lock.acquire();
            if (isRunning) {
              for (Supplier<?> source : sources) {
                ClerkUtil.pipe(source, processor);
              }
            }
            O output = processor.process();
            lock.release();
            return output;
          } catch (Exception e) {
          }
          return null;
        });
  }

  private void pipeData() {
    executor.submit(
        () -> {
          try {
            lock.acquire();
            for (Supplier<?> source : sources) {
              ClerkUtil.pipe(source, processor);
            }
            lock.release();
          } catch (Exception e) {
          }
        });
  }
}
