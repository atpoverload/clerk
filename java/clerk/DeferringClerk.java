package clerk;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import clerk.util.ClerkUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * A clerk that collects data on {@code start()} and {@code stop()}.
 *
 * <p>NOTE: Data operations are not done on the calling thread.
 */
public class DeferringClerk<O> implements Clerk<Future<O>> {
  private static final Logger logger = ClerkUtil.getLogger();
  private static final ThreadFactory daemonFactory =
      r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
      };

  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final ExecutorService executor = newSingleThreadExecutor(daemonFactory);
  private final ArrayList<Future<?>> dataFutures = new ArrayList<>();

  private boolean isRunning;

  public DeferringClerk(Supplier<?> source, Processor<?, O> processor) {
    this.sources = List.of(source);
    this.processor = processor;
  }

  public DeferringClerk(Iterable<Supplier<?>> sources, Processor<?, O> processor) {
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
  public final Future<O> read() {
    return executor.submit(
        () -> {
          synchronized (dataFutures) {
            // make sure the previous futures are done or cancelled
            for (Future<?> future : dataFutures) {
              // attempt to cancel the future; if we can't, get the result safely
              if (!future.isDone() && !future.isCancelled() && !future.cancel(false)) {
                try {
                  future.get();
                } catch (Exception e) {
                  logger.warning("could not consume a data future");
                  e.printStackTrace();
                }
              }
            }
            dataFutures.clear();
            return processor.process();
          }
        });
  }

  /** Shutdown the executor so the clerk cannot be reused. */
  public final void terminate() {
    executor.shutdown();
  }

  /** Pipes data from the sources to the processor on the calling thread. */
  private void startCollecting() {
    synchronized (dataFutures) {
      for (Supplier<?> source : sources) {
        dataFutures.add(executor.submit(() -> Clerk.pipe(source, processor)));
      }
    }
  }
}
