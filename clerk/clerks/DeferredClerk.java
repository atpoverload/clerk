package clerk.clerks;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import clerk.Processor;
import clerk.util.ClerkUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * A clerk that collects data on {@code start()} and {@code stop()} asynchronously. {@code read()}
 * will return a future that will collect data if the clerk is running, consume all other futures,
 * and return the output of the processor.
 *
 * <p>NOTE: This is a non-blocking implementation in contrast to the {@link DirectClerk}.
 */
public class DeferredClerk<O> implements SimpleClerk<Future<O>> {
  private static final Logger logger = ClerkUtil.getLogger();

  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;

  private final ExecutorService executor = newSingleThreadExecutor();
  private final ArrayList<Future<?>> dataFutures = new ArrayList<>();

  private final Semaphore lock = new Semaphore(1);

  private boolean isRunning = false;

  public DeferredClerk(Supplier<?> source, Processor<?, O> processor) {
    this.sources = List.of(source);
    this.processor = processor;
  }

  public DeferredClerk(Iterable<Supplier<?>> sources, Processor<?, O> processor) {
    this.sources = sources;
    this.processor = processor;
  }

  /**
   * Pipes a sample from each data source and puts them into the processor.
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
   * Pipes a sample from each data source and puts them into the processor.
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

  /** Returns a future of the processor output. */
  @Override
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
