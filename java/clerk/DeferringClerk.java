package clerk;

import clerk.util.ClerkUtil;
import java.util.ArrayList;
import java.util.Collection;
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
  private final ExecutorService executor;
  private final ArrayList<Future<?>> dataFutures = new ArrayList<>();

  private boolean isRunning;

  public DeferringClerk(Supplier<?> source, Processor<?, O> processor, ExecutorService executor) {
    this.sources = List.of(source);
    this.processor = processor;
    this.executor = executor;
  }

  public DeferringClerk(
      Collection<Supplier<?>> sources, Processor<?, O> processor, ExecutorService executor) {
    this.sources = sources;
    this.processor = processor;
    this.executor = executor;
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
      collectData();
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
      collectData();
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

  /** Pipes data from the sources to the processor on the calling thread. */
  private void collectData() {
    synchronized (dataFutures) {
      for (Supplier<?> source : sources) {
        dataFutures.add(executor.submit(() -> Clerk.pipe(source, processor)));
      }
    }
  }
}
